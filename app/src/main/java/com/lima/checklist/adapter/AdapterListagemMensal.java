package com.lima.checklist.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lima.checklist.R;
import com.lima.checklist.model.CheckListDiario;
import com.lima.checklist.model.CheckListMensal;
import com.lima.checklist.model.Funcionarios;
import com.lima.checklist.view.MainMensalActivity;
import com.lima.checklist.view.MensalActivity;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AdapterListagemMensal extends RecyclerView.Adapter<AdapterListagemMensal.MyViewHolder> {

    private static final int REQUEST_NOTIFICATION_PERMISSION = 1001;
    private static final String TAG = "AdapterListagem";  // Tag para logs

    private List<CheckListMensal> listagem;
    private Context context;

    Funcionarios func = new Funcionarios();

    public AdapterListagemMensal(List<CheckListMensal> listagem, Context context) {
        this.listagem = listagem;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listagem_mensal, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CheckListMensal checkListMensal = listagem.get(position);

        // Configurando os valores nos campos de texto
        holder.responsavel.setText(checkListMensal.getFuncionario().getNome());
        holder.horaPartida.setText(checkListMensal.getHoraPartida());
        holder.horaChegada.setText(checkListMensal.getHoraChegada());
        holder.data.setText(checkListMensal.getDataEntrega());
        holder.proxima.setText(checkListMensal.getProxima());
        holder.oleo.setText(checkListMensal.getOleo());
        holder.agua.setText(checkListMensal.getAgua());
        holder.bateria.setText(checkListMensal.getBateria());
        holder.hidraulico.setText(checkListMensal.getHidraulica());
        holder.elevacao.setText(checkListMensal.getElevacao());



        // Verificar a data de entrega para alterar a visibilidade do botão e solicitar permissão para enviar notificação
        verificarDataProximaEntrega(checkListMensal.getProxima(), holder.botaoAtualizarEntrega, checkListMensal.getNomeFuncExame(), context);

        // Adicionar ação ao botão
       holder.botaoAtualizarEntrega.setOnClickListener(v -> navegarParaEntregaEpiActivity(checkListMensal));
    }

    @Override
    public int getItemCount() {
        return listagem.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView responsavel;

        TextView data;
         TextView proxima;
        TextView horaPartida;
        TextView horaChegada;
        TextView oleo;
        TextView agua;
        TextView bateria;
        TextView hidraulico;
        TextView elevacao;
        Button botaoAtualizarEntrega;


        String chelistDiarioId;

        public MyViewHolder(View itemView) {
            super(itemView);


            responsavel = itemView.findViewById(R.id.textResponsavelMensal);
            horaPartida = itemView.findViewById(R.id.textViewHoraPartidaMensal);
            horaChegada = itemView.findViewById(R.id.textViewHoraChegadamensal);
            data = itemView.findViewById(R.id.textViewDataMensal);
            oleo = itemView.findViewById(R.id.textViewOleo);
            agua = itemView.findViewById(R.id.textViewAgua);
            bateria = itemView.findViewById(R.id.textViewBateria);
            hidraulico = itemView.findViewById(R.id.textViewHidraulico);
            elevacao = itemView.findViewById(R.id.textViewElevacao);
            proxima= itemView.findViewById(R.id.textViewProxima);
           botaoAtualizarEntrega = itemView.findViewById(R.id.btn_atualizar);
        }
    }

    private void verificarDataProximaEntrega(String dataProximaEntrega, Button botaoAtualizarEntrega, String nomeFuncionario,  Context context) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Verifica se a string de data não é nula ou vazia
        if (dataProximaEntrega == null || dataProximaEntrega.trim().isEmpty()) {
            Log.d(TAG, "Data de próxima entrega não preenchida. Ocultando botão.");
            botaoAtualizarEntrega.setVisibility(View.INVISIBLE);
            return; // Sai do método se a data não estiver preenchida
        }

        try {
            Log.d(TAG, "Verificando data de próxima entrega: " + dataProximaEntrega);
            Date dataEntrega = sdf.parse(dataProximaEntrega);
            Date dataAtual = new Date(); // Data atual

            if (dataEntrega != null && !dataEntrega.after(dataAtual)) {
                Log.d(TAG, "Data de entrega é igual ou anterior à data atual. Exibindo botão.");
                botaoAtualizarEntrega.setVisibility(View.VISIBLE);
                // Verificar e solicitar permissão para enviar notificações
                verificarPermissaoNotificacao(context, nomeFuncionario);
            } else {
                Log.d(TAG, "Data de entrega é posterior à data atual. Ocultando botão.");
                botaoAtualizarEntrega.setVisibility(View.INVISIBLE);
            }
        } catch (ParseException e) {
            Log.e(TAG, "Erro ao analisar a data de próxima entrega", e);
            botaoAtualizarEntrega.setVisibility(View.INVISIBLE);
        } catch (Exception e) {
            Log.e(TAG, "Erro ao verificar data de próxima entrega", e);
            botaoAtualizarEntrega.setVisibility(View.INVISIBLE);
        }
    }


    private void verificarPermissaoNotificacao(Context context, String nomeFuncionario) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permissão para notificações não concedida. Solicitando permissão.");

                // Verifica se o contexto é uma atividade antes de solicitar permissões
                if (context instanceof Activity) {
                    Activity activity = (Activity) context;
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_NOTIFICATION_PERMISSION);
                } else {
                    Log.e(TAG, "Contexto não é uma atividade, não é possível solicitar permissões.");
                }
            } else {
                Log.d(TAG, "Permissão para notificações concedida. Enviando notificação.");
                enviarNotificacao(context, "CheckList vencido",  nomeFuncionario + "O checklist  já venceu!");
            }
        } else {
            Log.d(TAG, "Versão do Android inferior ao 13. Enviando notificação sem solicitar permissão.");
            enviarNotificacao(context, "CheckList vencido", nomeFuncionario + " já venceu!");
        }
    }


    private void enviarNotificacao(Context context, String titulo, String mensagem) {
        String canalId = "Exame_Vencido_Canal";
        String canalNome = "Notificações de Exames Vencidos";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel canal = new NotificationChannel(canalId, canalNome, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(canal);
        }

        NotificationCompat.Builder notificacaoBuilder = new NotificationCompat.Builder(context, canalId)
                .setSmallIcon(R.drawable.logo_empilhadeira)
                .setContentTitle(titulo)
                .setContentText(mensagem)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notificationManager.notify(1, notificacaoBuilder.build());
    }

    private void navegarParaEntregaEpiActivity(@NonNull CheckListMensal checklist) {
        Intent intent = new Intent(context, MensalActivity.class);
        intent.putExtra("id", checklist.getId());
        intent.putExtra("idFuncionario", checklist.getFuncionario().getId());
        intent.putExtra("nome",checklist.getFuncionario().getNome());
        intent.putExtra("data", checklist.getDataEntrega());
        intent.putExtra("partida", checklist.getHoraPartida());
        intent.putExtra("chegada", checklist.getHoraChegada());
        intent.putExtra("oleo", checklist.getOleo());
        intent.putExtra("agua", checklist.getAgua());
        intent.putExtra("bateria", checklist.getBateria());
        intent.putExtra("hidraulico", checklist.getHidraulica());
        intent.putExtra("elevacao", checklist.getElevacao());
        intent.putExtra("proxima",checklist.getProxima());


        // Verifique se o contexto é uma Activity antes de iniciar
        if (context instanceof Activity) {
            context.startActivity(intent);
        } else {
            // Se context não for uma Activity, você pode precisar ajustar a lógica
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}

