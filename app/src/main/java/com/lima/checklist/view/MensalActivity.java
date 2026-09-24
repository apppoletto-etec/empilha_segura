package com.lima.checklist.view;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lima.checklist.R;
import com.lima.checklist.bd.ConfiguracaoFirebase;
import com.lima.checklist.model.CheckListDiario;
import com.lima.checklist.model.CheckListMensal;
import com.lima.checklist.model.Funcionarios;
import com.santalu.maskara.widget.MaskEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class MensalActivity extends AppCompatActivity {


    private FirebaseAuth autenticacao;

    private List<Funcionarios> funcionarioList = new ArrayList<>();
    private List<CheckListMensal> entregaExameList = new ArrayList<>();
    private ArrayAdapter<Funcionarios> arrayAdapterFuncionarios;
    private ArrayAdapter<CheckListMensal> arrayAdapterEntregaExames;

    private ListView  listTotalCheckListMensal;
    private ListView listVdadosFuncionario;

    FirebaseDatabase firebaseDatabaseEntregaExames;
    FirebaseDatabase firebaseDatabaseFuncionario;

    private Funcionarios funcionario = new Funcionarios();
    private CheckListMensal listDiario = new CheckListMensal();
    String nome1,funcao1,prazoEntrega1, nomeFuncEntrega1;

    private Button botaoEditarEntregaExames,botaoLimparEntregaExames,botaoExcluirEntregaExames, botaoSalvarEntregaEntregaExames;

    private EditText  dataEntrega,editNomePesquisa, editDataProxima;
       private MaskEditText campoHoraPartida, campoHoraChegada;
    private CheckBox checkOleo,checkAgua, checkBateria, checkHidarulico,checkElevacao;

    DatabaseReference funcionarioRef;
    DatabaseReference entregaExameRef;

    Funcionarios funcionarioSelecionado = new Funcionarios();

    Funcionarios funcionarioSelecionadoIntent = new Funcionarios();

    CheckListMensal entregSelecionada = new CheckListMensal();
    CheckListMensal entregSelecionadaIntent = new CheckListMensal();
    ImageView imageVoltar;
    Funcionarios funcId = new Funcionarios();

    private EditText editResponsavelMensal;

    String   textoOleo,textoAgua, textoBateria,textoHidraulico,textoElevacao;

    int cont = 0;

    private boolean flagIntent =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensal);

        // Habilita o botão "Up" na ActionBar



        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        Toolbar toolbar = findViewById(R.id.toolbarJava);
        toolbar.setTitle("Checklist Mensal");

        setSupportActionBar(toolbar);



       inicializarComponentes();
        inicializarFirebase();
       eventodatabaseFunc();
        eventoeditFunc();
        eventodatabaseExame();
        eventoeditExame();
      //  setupCheckBoxListeners();
        carregarEntrega();
        dataProximaTroca();
        dataEntrega();
        dataEntregaProxima();



        listVdadosFuncionario.setVisibility(View.INVISIBLE);
        botaoEditarEntregaExames.setVisibility(View.INVISIBLE);
        botaoExcluirEntregaExames.setVisibility(View.INVISIBLE);




        // Recuperar os dados da Intent (vindo do AdapterListagem)
        Intent intent = getIntent();
        if (intent != null) {
            String id = intent.getStringExtra("id");
            String idFuncionario = intent.getStringExtra("idFuncionario");


            // Verificar se os dados da entrega foram passados via Intent
            if (id != null && idFuncionario != null ) {
                // Se os dados vieram do Adapter, tornamos os campos invisíveis
                botaoLimparEntregaExames.setVisibility(View.INVISIBLE);
                botaoEditarEntregaExames.setVisibility(View.INVISIBLE);
                botaoSalvarEntregaEntregaExames.setVisibility(View.INVISIBLE);
                editNomePesquisa.setVisibility(View.INVISIBLE);

                botaoExcluirEntregaExames.setVisibility(View.VISIBLE);
                editResponsavelMensal.setEnabled(false);
                dataEntrega.setEnabled(false);

                listVdadosFuncionario.setVisibility(View.INVISIBLE);
                 listTotalCheckListMensal.setVisibility(View.INVISIBLE);
                checkOleo.setEnabled(false);
                checkAgua.setEnabled(false);

                checkHidarulico.setEnabled(false);
               checkAgua.setEnabled(false);
               checkElevacao.setEnabled(false);
               checkBateria.setEnabled(false);


                editDataProxima.setEnabled(false);
                campoHoraPartida.setEnabled(false);
                campoHoraChegada.setEnabled(false);



                flagIntent =false;

                //  Intent i = new Intent(this, MainActivity.class);
                //  startActivity(i);

            } else {
                // Se a Activity foi aberta sem os dados do Adapter (ou seja, em modo de criação)
                botaoLimparEntregaExames.setVisibility(View.VISIBLE);
                botaoEditarEntregaExames.setVisibility(View.INVISIBLE);
                botaoSalvarEntregaEntregaExames.setVisibility(View.VISIBLE);
                botaoExcluirEntregaExames.setVisibility(View.INVISIBLE);
                editNomePesquisa.setVisibility(View.VISIBLE);
                editResponsavelMensal.setEnabled(true);
                dataEntrega.setEnabled(true);
           //     dataProximaEntrega.setEnabled(true);

                checkOleo.setEnabled(true);
                checkAgua.setEnabled(true);
            //    checkInapto.setEnabled(true);
            //   checkApto.setEnabled(true);
                checkHidarulico.setEnabled(true);
                checkAgua.setEnabled(true);
                checkElevacao.setEnabled(true);
                checkBateria.setEnabled(true);

                editDataProxima.setEnabled(true);
                campoHoraPartida.setEnabled(true);
                campoHoraChegada.setEnabled(true);

                listVdadosFuncionario.setVisibility(View.INVISIBLE);
                listTotalCheckListMensal.setVisibility(View.INVISIBLE);

                flagIntent =true;
            }
        }











        //inicializando os lists
        //carregando funcionário  na lista
        listVdadosFuncionario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                funcionarioSelecionado =(Funcionarios)parent.getItemAtPosition(position);

                editResponsavelMensal.setText(funcionarioSelecionado.getNome().toString());
            //    campoFuncao.setText(funcionarioSelecionado.getFuncao().toString());
                System.out.println(funcionarioSelecionado.getId());



                nome1 =funcionarioSelecionado.getNome().toString();
                funcao1=funcionarioSelecionado.getFuncao().toString();
                  listVdadosFuncionario.setVisibility(View.INVISIBLE);

            }
        });


         listTotalCheckListMensal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                entregSelecionada =(CheckListMensal) parent.getItemAtPosition(position);

                // Preenche os campos de texto com os dados selecionados
                editResponsavelMensal.setText(entregSelecionada.getNomeFuncExame());
                dataEntrega.setText(entregSelecionada.getDataEntrega());
                editDataProxima.setText(entregSelecionada.getProxima());
                campoHoraPartida.setText(entregSelecionada.getHoraPartida());
                campoHoraChegada.setText(entregSelecionada.getHoraChegada());
                checkOleo.setChecked("Checado".equals(entregSelecionada.getOleo()));
                checkAgua.setChecked("Checado".equals(entregSelecionada.getAgua()));
                checkBateria.setChecked("Checado".equals(entregSelecionada.getBateria()));
                checkHidarulico.setChecked("Checado".equals(entregSelecionada.getHidraulica()));
                checkElevacao.setChecked("Checado".equals(entregSelecionada.getElevacao()));




                funcId = entregSelecionada.getFuncionario();

                // Exibe ou oculta os botões conforme necessário
                botaoEditarEntregaExames.setVisibility(View.VISIBLE);
                botaoExcluirEntregaExames.setVisibility(View.VISIBLE);
                botaoSalvarEntregaEntregaExames.setVisibility(View.INVISIBLE);
                listVdadosFuncionario.setVisibility(View.INVISIBLE);
            }
        });




    }//fim onCreate








    public void carregarEntrega() {
        // Recuperar dados da Intent
        Intent intent = getIntent();
        String id = null;
        String partida = null;
        String chegada = null;
        String nome = null;
        String dataEntregaIntent = null;
        String proximaEntregaIntent = null;
        String oleo =null;
        String agua = null;
        String bateria  =null;
        String hidraulico =null;
        String elevacao = null;


        if (intent != null) {
            id = intent.getStringExtra("id");
            String idFuncionario = intent.getStringExtra("idFuncionario");
            nome = intent.getStringExtra("nome");
             partida = intent.getStringExtra("partida");


            dataEntregaIntent = intent.getStringExtra("data");
             proximaEntregaIntent = intent.getStringExtra("proxima");

            oleo = intent.getStringExtra("oleo");
            agua = intent.getStringExtra("agua");
            chegada = intent.getStringExtra("chegada");
            bateria = intent.getStringExtra("bateria");
            hidraulico = intent.getStringExtra("hidraulico");
            elevacao = intent.getStringExtra("elevacao");

            // Inicializar os objetos antes de setar os valores
            funcionarioSelecionadoIntent = new Funcionarios();


            // Preencher os campos da entrega
            entregSelecionadaIntent.setId(id);

            // Preencher o Funcionario
            if (idFuncionario != null) {
                funcionarioSelecionadoIntent.setId(idFuncionario);
                funcionarioSelecionadoIntent.setNome(nome);

                // Preencha com o nome recuperado
            }


        }

        // Preencher os dados da entrega na entrega selecionada

        entregSelecionadaIntent = new CheckListMensal();
        entregSelecionadaIntent.setId(id);
        entregSelecionadaIntent.setFuncionario(funcionarioSelecionadoIntent);
        entregSelecionadaIntent.setDataEntrega(dataEntregaIntent);
        entregSelecionadaIntent.setProxima(proximaEntregaIntent);
        entregSelecionadaIntent.setHoraPartida(partida);
        entregSelecionadaIntent.setHoraChegada(chegada);
        checkOleo.setChecked("Checado".equals(oleo));
        checkAgua.setChecked("Checado".equals(agua));
        checkBateria.setChecked("Checado".equals(bateria));
        checkHidarulico.setChecked("Checado".equals(hidraulico));
        checkElevacao.setChecked("Checado".equals(elevacao));



         editResponsavelMensal.setText(nome);
        dataEntrega.setText(dataEntregaIntent);


         campoHoraChegada.setText(chegada);
         campoHoraPartida.setText(partida);



      editDataProxima.setText(proximaEntregaIntent);
    }



    //-----crud---------


    //salvar texto formatado no firebase
      //
    private String formatarTexto(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }

        // Divide o texto em palavras
        String[] palavras = texto.split("\\s+");
        StringBuilder textoFormatado = new StringBuilder();

        for (String palavra : palavras) {
            if (!palavra.isEmpty()) {
                // Converte a primeira letra para maiúscula e o restante para minúsculo
                String palavraFormatada = palavra.substring(0, 1).toUpperCase() + palavra.substring(1).toLowerCase();
                textoFormatado.append(palavraFormatada).append(" ");
            }
        }

        // Remove o último espaço em branco
        return textoFormatado.toString().trim();
    }


    public void salvarEntregaExames(View v) {
        entregaExameRef = ConfiguracaoFirebase.getFireBaseDatabase();

        // Formata o texto de entrada
        String textoNome = formatarTexto(editResponsavelMensal.getText().toString());
        String dataEntreg = formatarTexto(dataEntrega.getText().toString());
        String proxima = formatarTexto(editDataProxima.getText().toString());
        String horaPartida = campoHoraPartida.getMasked();
        String horaChegada = campoHoraChegada.getMasked();

        // Recupera os valores dos checkboxes e formata

        selecionarCheckBox();


        if(!textoNome.isEmpty()){
            if(!dataEntreg.isEmpty()){
                if(!horaPartida.isEmpty()){
                    funcionario = new Funcionarios();
                    funcionario.setNome(funcionarioSelecionado.getNome());
                    funcionario.setFuncao(funcionarioSelecionado.getFuncao());
                    funcionario.setId(funcionarioSelecionado.getId());

                    listDiario = new CheckListMensal();

                    listDiario.setFuncionario(funcionario);
                    listDiario.setNomeFuncExame(textoNome.trim());
                    listDiario.setDataEntrega(dataEntreg.trim());
                    listDiario.setProxima(proxima.toString());
                   listDiario.setOleo(textoOleo);
                   listDiario.setAgua(textoAgua);
                   listDiario.setBateria(textoBateria);
                   listDiario.setHidraulica(textoHidraulico);
                   listDiario.setElevacao(textoElevacao);
                    listDiario.setHoraPartida(horaPartida.toString());
                    listDiario.setHoraChegada(horaChegada.toString());
                    listDiario.setDataEntrega(dataEntrega.getText().toString().trim());
                    listDiario.setId(UUID.randomUUID().toString());

                    entregaExameRef.child("CheckListMensal").child(listDiario.getId()).setValue(listDiario);

                    Toast.makeText(this, "Salvo!", Toast.LENGTH_SHORT).show();
                    limparCampos();
                    editResponsavelMensal.requestFocus();

                }else{
                    Toast.makeText(this, "Digite a hora de partida", Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(this, "Digite a data do checklist", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Digite o nome do responsável", Toast.LENGTH_SHORT).show();
        }




    }

    //fim salvar
    public void atualizarEntregaExame(View v) {

        // Formata o texto de entrada
        String textoNome = formatarTexto(editResponsavelMensal.getText().toString());
        String dataEntreg = formatarTexto(dataEntrega.getText().toString());
        String horaPartida = campoHoraPartida.getMasked();
        String horaChegada = campoHoraChegada.getMasked();
        String proxima = formatarTexto(editDataProxima.getText().toString());

        // Recupera os valores dos checkboxes e formata

           selecionarCheckBox();

        if (!textoNome.isEmpty()) {

            funcionario = new Funcionarios();

            // Verificando nulidade do funcionario selecionado
            if (funcionarioSelecionado != null && funcionarioSelecionado.getId() != null && funcionarioSelecionado.getNome() != null) {
                funcionario.setNome(funcionarioSelecionado.getNome());
                funcionario.setFuncao(funcionarioSelecionado.getFuncao());
                funcionario.setId(funcionarioSelecionado.getId());
            } else {
                funcionario.setId(funcId.getId());
                funcionario.setNome(funcId.getNome());
                funcionario.setFuncao(funcId.getFuncao());
            }

            listDiario.setFuncionario(funcionario);
            listDiario.setNomeFuncExame(textoNome.trim());
            listDiario.setDataEntrega(dataEntreg.trim());
            listDiario.setProxima(proxima.toString());
            listDiario.setOleo(textoOleo);
            listDiario.setAgua(textoAgua);
            listDiario.setBateria(textoBateria);
            listDiario.setHidraulica(textoHidraulico);
            listDiario.setElevacao(textoElevacao);
            listDiario.setHoraPartida(horaPartida.toString());
            listDiario.setHoraChegada(horaChegada.toString());
            listDiario.setDataEntrega(dataEntrega.getText().toString().trim());
            listDiario.setId(entregSelecionada.getId());
          //  listDiario.setId(UUID.randomUUID().toString());



            if (listDiario.getId() != null) {
                entregaExameRef.child("CheckListMensal").child(listDiario.getId()).setValue(listDiario)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Registro atualizado!", Toast.LENGTH_SHORT).show();
                            limparCampos();
                            editResponsavelMensal.requestFocus();
                            entregSelecionada.setId(null);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Erro ao atualizar registro: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(this, "Erro: ID do exame é nulo.", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Você precisa inserir os registros que deseja alterar", Toast.LENGTH_SHORT).show();
        }
    }


    public void selecionarCheckBox(){

        if(checkOleo.isChecked()){
             textoOleo = "Checado";
        }else{
            textoOleo = "Não checado";
        }
        if(checkAgua.isChecked()){
            textoAgua = "Checado";
        }else{
            textoAgua= "Não checados";
        }
        if(checkBateria.isChecked()){
            textoBateria = "Checado";
        }else{
            textoBateria = "Não checados";
        }
        if(checkHidarulico.isChecked()){
            textoHidraulico= "Checado";
        }else{
            textoHidraulico = "Não checado";
        }
        if(checkElevacao.isChecked()){
            textoElevacao = "Checado";
        }else{
            textoElevacao = "Não checado";
        }
    }


    public void deletar(View v){
        //instancia objeto dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        //configura titulo da mensagem
        dialog.setTitle("Deletar registro");

        dialog.setIcon(getResources().getDrawable(R.drawable.baseline_close_24));
        dialog.setMessage("Tem certeza que deseja dar baixa neste registro ?");

        //configura ações
        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(entregSelecionada!=null && entregSelecionada.getId()!=null&&entregSelecionada.getNomeFuncExame()!=null) {
                    CheckListMensal e = new CheckListMensal();
                    e.setId(entregSelecionada.getId());
                    entregaExameRef.child("CheckListMensal").child(e.getId()).removeValue();

                    limparCampos();
                    botaoLimparEntregaExames.setVisibility(View.VISIBLE);
                      botaoEditarEntregaExames.setVisibility(View.INVISIBLE);
                    botaoSalvarEntregaEntregaExames.setVisibility(View.VISIBLE);
                    botaoExcluirEntregaExames.setVisibility(View.INVISIBLE);
                    editNomePesquisa.setVisibility(View.VISIBLE);
                    editResponsavelMensal.setEnabled(true);
                    dataEntrega.setEnabled(true);
                    //     dataProximaEntrega.setEnabled(true);

                    checkOleo.setEnabled(true);
                    checkAgua.setEnabled(true);
                    //    checkInapto.setEnabled(true);
                    //   checkApto.setEnabled(true);
                    checkHidarulico.setEnabled(true);
                    checkAgua.setEnabled(true);
                    checkElevacao.setEnabled(true);
                    checkBateria.setEnabled(true);

                    editDataProxima.setEnabled(true);
                    campoHoraPartida.setEnabled(true);
                    campoHoraChegada.setEnabled(true);

                }else{
                    CheckListMensal e = new CheckListMensal();
                    e.setId(entregSelecionadaIntent.getId());
                    entregaExameRef.child("CheckListMensal").child(e.getId()).removeValue();

                    limparCampos();
                    botaoLimparEntregaExames.setVisibility(View.VISIBLE);
                    botaoEditarEntregaExames.setVisibility(View.INVISIBLE);
                    botaoSalvarEntregaEntregaExames.setVisibility(View.VISIBLE);
                    botaoExcluirEntregaExames.setVisibility(View.INVISIBLE);
                    editNomePesquisa.setVisibility(View.VISIBLE);
                    editResponsavelMensal.setEnabled(true);
                    dataEntrega.setEnabled(true);
                    //     dataProximaEntrega.setEnabled(true);

                    checkOleo.setEnabled(true);
                    checkAgua.setEnabled(true);
                    //    checkInapto.setEnabled(true);
                    //   checkApto.setEnabled(true);
                    checkHidarulico.setEnabled(true);
                    checkAgua.setEnabled(true);
                    checkElevacao.setEnabled(true);
                    checkBateria.setEnabled(true);

                    editDataProxima.setEnabled(true);
                    campoHoraPartida.setEnabled(true);
                    campoHoraChegada.setEnabled(true);
                }


                Toast.makeText(MensalActivity.this, "Registro excluido", Toast.LENGTH_SHORT).show();

            }
        });

        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        //criar exibir dialog
        dialog.create();
        dialog.show();




    }



    //--fim crud--------


    ///métodos de evento de listagem

    private void eventoeditFunc() {


        editResponsavelMensal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String palavra= editResponsavelMensal.getText().toString().trim();
                pesquisaPalavraFunc(palavra);

            }
        });
    }

    private void eventodatabaseFunc() {
        //-----------------------------------

        funcionarioRef.child("Funcionario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                funcionarioList.clear();
                for(DataSnapshot objSnapshot: dataSnapshot.getChildren()){
                    Funcionarios s=objSnapshot.getValue(Funcionarios.class);
                    funcionarioList.add(s);
                    cont= funcionarioList.size();
                    //  totalFunc.setText(String.valueOf(cont));

                }
                arrayAdapterFuncionarios = new ArrayAdapter<Funcionarios>(MensalActivity.this,
                        android.R.layout.simple_list_item_1,funcionarioList );
                     listVdadosFuncionario.setAdapter(arrayAdapterFuncionarios);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void pesquisaPalavraFunc(@NonNull String palavra) {

        listVdadosFuncionario.setVisibility(View.VISIBLE);

        String textoDigitado = formatarTexto(palavra);

        Query query;
        if (textoDigitado.equals("")) {
            query =funcionarioRef.child("Funcionario").orderByChild("nome");
        } else {



            query = funcionarioRef.child("Funcionario")
                    .orderByChild("nome")
                    .startAt(textoDigitado).endAt(textoDigitado + "\uf8ff");

            nome1 = textoDigitado;
        }

        funcionarioList.clear();

        //------
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objsnapshot : dataSnapshot.getChildren()) {
                    Funcionarios s = objsnapshot.getValue(Funcionarios.class);
                    funcionarioList.add(s);



                }

                arrayAdapterFuncionarios = new ArrayAdapter<Funcionarios>(MensalActivity.this,
                        android.R.layout.simple_list_item_1, funcionarioList);
                if (nome1 == null) {
                    arrayAdapterFuncionarios.clear();
                }
                listVdadosFuncionario.setAdapter(arrayAdapterFuncionarios);



            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void pesquisaPalavraExame(@NonNull String palavra) {

         listTotalCheckListMensal.setVisibility(View.VISIBLE);

        String textoDigitado = formatarTexto(palavra);

        Query query;
        if (textoDigitado.equals("")) {
            query =entregaExameRef.child("CheckListmensal").orderByChild("nomeFuncExame");
        } else {



            query = entregaExameRef.child("CheckListMensal")
                    .orderByChild("nomeFuncExame")
                    .startAt(textoDigitado).endAt(textoDigitado + "\uf8ff");

            nome1 = textoDigitado;
        }

        entregaExameList.clear();

        //------
        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objsnapshot : dataSnapshot.getChildren()) {
                    CheckListMensal e = objsnapshot.getValue(CheckListMensal.class);
                   entregaExameList.add(e);



                }

                arrayAdapterEntregaExames = new ArrayAdapter<CheckListMensal>(MensalActivity.this,
                        android.R.layout.simple_list_item_1, entregaExameList);
                if (nome1 == null) {
                    arrayAdapterEntregaExames.clear();
                }
                 listTotalCheckListMensal.setAdapter(arrayAdapterEntregaExames);



            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void eventodatabaseExame() {
        //-----------------------------------

        entregaExameRef.child("CheckListMensal").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                entregaExameList.clear();
                for(DataSnapshot objSnapshot: dataSnapshot.getChildren()){
                    CheckListMensal s=objSnapshot.getValue(CheckListMensal.class);
                    entregaExameList.add(s);
                    cont= entregaExameList.size();
                    //  totalFunc.setText(String.valueOf(cont));

                }
                arrayAdapterEntregaExames = new ArrayAdapter<CheckListMensal>(MensalActivity.this,
                        android.R.layout.simple_list_item_1,entregaExameList );
                 listTotalCheckListMensal.setAdapter(arrayAdapterEntregaExames);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void eventoeditExame() {


       editNomePesquisa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String palavra= editNomePesquisa.getText().toString().trim();
                pesquisaPalavraExame(palavra);

            }
        });
    }

    //----------------------------------fim metodos evento de listagem





     private void inicializarComponentes() {

        editResponsavelMensal = findViewById(R.id.editTextResponsavelMensal);
        dataEntrega = findViewById(R.id.editDataMensal);
         editDataProxima = findViewById(R.id.editProximaMensal);
        checkOleo = findViewById(R.id.checkBoxOleo);
        checkAgua = findViewById(R.id.checkBoxAgua);
        checkBateria = findViewById(R.id.checkBoxBateria);
        checkHidarulico = findViewById(R.id.checkBoxHidraulica);
        checkElevacao = findViewById(R.id.checkBoxElevacao);
         listTotalCheckListMensal = findViewById(R.id.listTotalCheckListMensal);
        listVdadosFuncionario= findViewById(R.id.listFuncionarioExames);
        botaoSalvarEntregaEntregaExames= findViewById(R.id.buttonSalvarExame);
        botaoEditarEntregaExames = findViewById(R.id.buttonAtualizarExame);
        botaoExcluirEntregaExames = findViewById(R.id.buttonExcluirExame);
        botaoLimparEntregaExames = findViewById(R.id.buttonLimparExame);
         editNomePesquisa = findViewById(R.id.editNomePesquisarChecklistmensal);



         campoHoraPartida = findViewById(R.id.editTextPartidaMensal);

         campoHoraChegada= findViewById(R.id.editTextChegadaMensal);

    }

//    public  void voltarMain(View v){
//        Intent intent = new Intent(DiarioActivity.this, ActivityListagemChecklistDiarioBinding.class);
//        startActivity(intent);
//    }


    public void inicializarFirebase(){

        firebaseDatabaseFuncionario    =FirebaseDatabase.getInstance();
        if (FirebaseApp.getApps(this).size() == 0)
            firebaseDatabaseFuncionario.setPersistenceEnabled(true);
        funcionarioRef =firebaseDatabaseFuncionario.getReference();

        firebaseDatabaseEntregaExames=FirebaseDatabase.getInstance();
        if (FirebaseApp.getApps(this).size() == 0)
            firebaseDatabaseEntregaExames.setPersistenceEnabled(true);
        entregaExameRef= firebaseDatabaseEntregaExames .getReference();
        FirebaseApp.initializeApp(MensalActivity.this);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_mensal,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.sair) {
            deslogarUsuario();
            finish();
        } else if (id == R.id.cadastroFunc) {
            Intent intent = new Intent(MensalActivity.this, CadastroFuncionarioActivity .class);

            startActivity(intent);
            finish();

        }else if(id ==R.id.relatorio){
            Intent intent = new Intent(MensalActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else if(id ==R.id.relatorioMensal){
            Intent intent = new Intent(MensalActivity.this, MainMensalActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id ==R.id.diario){
            Intent intent = new Intent(MensalActivity.this, DiarioActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    public void deslogarUsuario(){

try{
autenticacao.signOut();
}catch (Exception e){
    e.printStackTrace();
}


}




    private void limparCampos() {

        editResponsavelMensal.setText("");
        dataEntrega.setText("");

        checkOleo.setChecked(false);
       checkAgua.setChecked(false);
       checkBateria.setChecked(false);
        checkElevacao.setChecked(false);
        checkHidarulico.setChecked(false);
       campoHoraChegada.setText("");
        campoHoraPartida.setText("");
        editNomePesquisa.setText("");
        listVdadosFuncionario.setVisibility(View.INVISIBLE);
         listTotalCheckListMensal.setVisibility(View.INVISIBLE);
       editDataProxima.setText("");




    }

    public void limparCamposBotaoNovo(View v) {

        editResponsavelMensal.setText("");
        dataEntrega.setText("");

        checkOleo.setChecked(false);
        checkAgua.setChecked(false);
        checkBateria.setChecked(false);
        checkElevacao.setChecked(false);
        checkHidarulico.setChecked(false);
        editDataProxima.setText("");
        campoHoraChegada.setText("");
        campoHoraPartida.setText("");
        editNomePesquisa.setText("");
        listVdadosFuncionario.setVisibility(View.INVISIBLE);
         listTotalCheckListMensal.setVisibility(View.INVISIBLE);
        botaoSalvarEntregaEntregaExames.setVisibility(View.VISIBLE);
        botaoExcluirEntregaExames.setVisibility(View.INVISIBLE);
        botaoEditarEntregaExames.setVisibility(View.INVISIBLE);





    }


    public  void dataProximaTroca(){
        editDataProxima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MensalActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                calendar.set(year, month, day);
                                String format = "dd/MM/yyyy";
                                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
                                Date date;

                                try {
                                    date = sdf.parse(sdf.format(calendar.getTime()));
                                    String dayS = new SimpleDateFormat("dd", Locale.ENGLISH).format(date);
                                    String monthS = new SimpleDateFormat("MM", Locale.ENGLISH).format(date);
                                    String yearS = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(date);

                                   editDataProxima.setText((dayS + "/" + monthS + "/" + yearS));
                                } catch (ParseException ignored) {

                                }
                            }
                        }, year, month, day);
                datePickerDialog.show();
                datePickerDialog.getDatePicker();
            }


        });


        String dataProximaEntregas  = editDataProxima.getText().toString();

    }


    public  void dataEntrega(){
       dataEntrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MensalActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                calendar.set(year, month, day);
                                String format = "dd/MM/yyyy";
                                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
                                Date date;

                                try {
                                    date = sdf.parse(sdf.format(calendar.getTime()));
                                    String dayS = new SimpleDateFormat("dd", Locale.ENGLISH).format(date);
                                    String monthS = new SimpleDateFormat("MM", Locale.ENGLISH).format(date);
                                    String yearS = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(date);

                                    dataEntrega.setText((dayS + "/" + monthS + "/" + yearS));
                                } catch (ParseException ignored) {

                                }
                            }
                        }, year, month, day);
                datePickerDialog.show();
                datePickerDialog.getDatePicker();
            }


        });


        String dataEntreg  = dataEntrega.getText().toString();

    }


    public  void dataEntregaProxima(){
        editDataProxima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MensalActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                calendar.set(year, month, day);
                                String format = "dd/MM/yyyy";
                                SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
                                Date date;

                                try {
                                    date = sdf.parse(sdf.format(calendar.getTime()));
                                    String dayS = new SimpleDateFormat("dd", Locale.ENGLISH).format(date);
                                    String monthS = new SimpleDateFormat("MM", Locale.ENGLISH).format(date);
                                    String yearS = new SimpleDateFormat("yyyy", Locale.ENGLISH).format(date);

                                    editDataProxima.setText((dayS + "/" + monthS + "/" + yearS));
                                } catch (ParseException ignored) {

                                }
                            }
                        }, year, month, day);
                datePickerDialog.show();
                datePickerDialog.getDatePicker();
            }


        });


        String dataproxima  = editDataProxima.getText().toString();

    }


}