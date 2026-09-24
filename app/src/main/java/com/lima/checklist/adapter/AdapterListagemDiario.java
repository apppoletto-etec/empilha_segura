package com.lima.checklist.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.lima.checklist.R;

import com.lima.checklist.model.CheckListDiario;
import com.lima.checklist.model.Funcionarios;
import com.lima.checklist.view.MainActivity;


import java.util.List;


public class AdapterListagemDiario extends RecyclerView.Adapter<AdapterListagemDiario.MyViewHolder> {

    private static final int REQUEST_NOTIFICATION_PERMISSION = 1001;
    private static final String TAG = "AdapterListagem";  // Tag para logs

    private List<CheckListDiario> listagem;
    private Context context;

    Funcionarios func = new Funcionarios();

    public AdapterListagemDiario(List<CheckListDiario> listagem, Context context) {
        this.listagem = listagem;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_listagem, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CheckListDiario checkdiario = listagem.get(position);

        // Configurando os valores nos campos de texto
        holder.responsavel.setText(checkdiario.getFuncionario().getNome());
        holder.horaPartida.setText(checkdiario.getHoraPartida());
        holder.horaChegada.setText(checkdiario.getHoraChegada());
        holder.data.setText(checkdiario.getDataEntrega());
        holder.pneu.setText(checkdiario.getPneu());
        holder.freio.setText(checkdiario.getFreios());
        holder.farol.setText(checkdiario.getFarois());
        holder.buzina.setText(checkdiario.getBuzina());
        holder.combustivel.setText(checkdiario.getCombustivel());


        // Verificar a data de entrega para alterar a visibilidade do botão e solicitar permissão para enviar notificação
     //   verificarDataProximaEntrega(entregaExame.getDataProximaEntrega(), holder.botaoAtualizarEntrega, entregaExame.getNomeFuncExame(), entregaExame.getTipoExame(), context);

        // Adicionar ação ao botão
      //  holder.botaoAtualizarEntrega.setOnClickListener(v -> navegarParaEntregaEpiActivity(entregaExame));
    }

    @Override
    public int getItemCount() {
        return listagem.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView responsavel;

        TextView data;

        TextView horaPartida;
        TextView horaChegada;
        TextView pneu;
        TextView freio;
        TextView farol;
        TextView buzina;
        TextView combustivel;


        String chelistDiarioId;

        public MyViewHolder(View itemView) {
            super(itemView);


            responsavel = itemView.findViewById(R.id.textResponsavelMensal);
            horaPartida = itemView.findViewById(R.id.textViewHoraPartidaMensal);
            horaChegada = itemView.findViewById(R.id.textViewHoraChegadamensal);
            data = itemView.findViewById(R.id.textViewDataMensal);
            pneu = itemView.findViewById(R.id.textViewPneu);
            freio = itemView.findViewById(R.id.textViewFreios);
            farol = itemView.findViewById(R.id.textViewFarois);
            buzina = itemView.findViewById(R.id.textViewBuzina);
            combustivel = itemView.findViewById(R.id.textViewCombustivel);
        }
    }

//    private void verificarDataProximaEntrega(String dataProximaEntrega, Button botaoAtualizarEntrega, String nomeFuncionario, String tipoExame, Context context) {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//
//        // Verifica se a string de data não é nula ou vazia
//        if (dataProximaEntrega == null || dataProximaEntrega.trim().isEmpty()) {
//            Log.d(TAG, "Data de próxima entrega não preenchida. Ocultando botão.");
//            botaoAtualizarEntrega.setVisibility(View.INVISIBLE);
//            return; // Sai do método se a data não estiver preenchida
//        }
//
//        try {
//            Log.d(TAG, "Verificando data de próxima entrega: " + dataProximaEntrega);
//            Date dataEntrega = sdf.parse(dataProximaEntrega);
//            Date dataAtual = new Date(); // Data atual
//
//            if (dataEntrega != null && !dataEntrega.after(dataAtual)) {
//                Log.d(TAG, "Data de entrega é igual ou anterior à data atual. Exibindo botão.");
//                botaoAtualizarEntrega.setVisibility(View.VISIBLE);
//                // Verificar e solicitar permissão para enviar notificações
//                verificarPermissaoNotificacao(context, nomeFuncionario, tipoExame);
//            } else {
//                Log.d(TAG, "Data de entrega é posterior à data atual. Ocultando botão.");
//                botaoAtualizarEntrega.setVisibility(View.INVISIBLE);
//            }
//        } catch (ParseException e) {
//            Log.e(TAG, "Erro ao analisar a data de próxima entrega", e);
//            botaoAtualizarEntrega.setVisibility(View.INVISIBLE);
//        } catch (Exception e) {
//            Log.e(TAG, "Erro ao verificar data de próxima entrega", e);
//            botaoAtualizarEntrega.setVisibility(View.INVISIBLE);
//        }
//    }


    private void verificarPermissaoNotificacao(Context context, String nomeFuncionario, String tipoExame) {
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
                enviarNotificacao(context, "Exame vencido", "O exame " + tipoExame + " de " + nomeFuncionario + " já venceu!");
            }
        } else {
            Log.d(TAG, "Versão do Android inferior ao 13. Enviando notificação sem solicitar permissão.");
            enviarNotificacao(context, "Exame vencido", "O exame " + tipoExame + " de " + nomeFuncionario + " já venceu!");
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

    private void navegarParaEntregaEpiActivity(@NonNull CheckListDiario checklist) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("id", checklist.getId());
        intent.putExtra("idFuncionario", checklist.getFuncionario().getId());
        intent.putExtra("data", checklist.getDataEntrega());
        intent.putExtra("partida", checklist.getHoraPartida());
        intent.putExtra("chegada", checklist.getHoraChegada());
        intent.putExtra("pneu", checklist.getPneu());
        intent.putExtra("freio", checklist.getFreios());
        intent.putExtra("farol", checklist.getFarois());
        intent.putExtra("buzina", checklist.getBuzina());
        intent.putExtra("combustivel", checklist.getCombustivel());


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

