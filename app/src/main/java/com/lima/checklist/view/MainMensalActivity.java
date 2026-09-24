package com.lima.checklist.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.lima.checklist.R;
import com.lima.checklist.adapter.AdapterListagemDiario;
import com.lima.checklist.adapter.AdapterListagemMensal;
import com.lima.checklist.bd.ConfiguracaoFirebase;
import com.lima.checklist.model.CheckListDiario;
import com.lima.checklist.model.CheckListMensal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainMensalActivity extends AppCompatActivity {

    private EditText campoFuncionarioListagem;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference listagemRef;
    private FirebaseAuth autenticacao;
    private RecyclerView recyclerListagem;
    private List<CheckListMensal> checklistList = new ArrayList<>();
    private AdapterListagemMensal adapterListagem;
    private Button botaoAtualizarEntrega;

    private ProgressBar progressBarMensal;


    // Listener do Firebase para ser removido quando a atividade for destruída
    private ValueEventListener listagemListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_mensal);
        inicializarComponentes();

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        listagemRef = ConfiguracaoFirebase.getFireBaseDatabase().child("CheckListMensal");

        Toolbar toolbar = findViewById(R.id.toolbarJava);
        toolbar.setTitle("Relatório de checklist mensal");
        setSupportActionBar(toolbar);


//        // Agendar a verificação diária
//        PeriodicWorkRequest verificacaoDiariaWork =
//                new PeriodicWorkRequest.Builder(VerificacaoDiariaWorker.class, 1, TimeUnit.DAYS)
//                        .build();
//
//        WorkManager.getInstance(this).enqueue(verificacaoDiariaWork);






        // Configurar RecyclerView
        recyclerListagem.setLayoutManager(new LinearLayoutManager(this));
        recyclerListagem.setHasFixedSize(true);

        // Inicializar o Adapter
        adapterListagem = new AdapterListagemMensal (checklistList, this);
        recyclerListagem.setAdapter(adapterListagem);

        // Recupera a listagem de entregas de EPI
        recuperaListagem();

        // Configura o evento de edição para pesquisa
        eventoEditListagem();
    }

    private void pesquisaEntrega(@NonNull String palavra) {
        String textoDigitado = palavra;
        Query query;

        if (textoDigitado.equals("")) {
            query = listagemRef.orderByChild("nomeFuncExame");
        } else {
            query = listagemRef
                    .orderByChild("nomeFuncExame")
                    .startAt(textoDigitado)
                    .endAt(textoDigitado + "\uf8ff");
        }

        checklistList.clear();

        listagemListener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                checklistList.clear(); // Limpa a lista antes de adicionar os itens filtrados

                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                    CheckListMensal e = objSnapshot.getValue(CheckListMensal.class);
                    checklistList.add(e);
                }

                adapterListagem.notifyDataSetChanged(); // Notifica o adaptador sobre as mudanças
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Trate o erro, se necessário
            }
        });
    }

    private void eventoEditListagem() {
        campoFuncionarioListagem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String palavra = campoFuncionarioListagem.getText().toString().trim();
                pesquisaEntrega(palavra);
            }
        });
    }

    private void recuperaListagem() {
        progressBarMensal.setVisibility(View.VISIBLE);
        listagemListener = listagemRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                checklistList.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    checklistList.add(ds.getValue(CheckListMensal.class));
                }

                Collections.reverse(checklistList);
                adapterListagem.notifyDataSetChanged();
                progressBarMensal.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (checklistList == null) {
                    Toast.makeText(MainMensalActivity.this, "Não há itens", Toast.LENGTH_SHORT).show();
                }
                progressBarMensal.setVisibility(View.GONE);
            }
        });
    }

    private void inicializarComponentes() {
        recyclerListagem = findViewById(R.id.recyclerListagem);
      //  botaoAtualizarEntrega = findViewById(R.id.btnAtualizarEntrega);
        campoFuncionarioListagem = findViewById(R.id.editPesquisarListagem);
        progressBarMensal = findViewById(R.id.progressBarMensal);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_tela_relatorio_mensal, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sair) {
            deslogarUsuario();
            finish();
        } else if (id == R.id.cadastroFunc) {
            startActivity(new Intent(MainMensalActivity.this, CadastroFuncionarioActivity.class));
            finish();
        } else if (id == R.id.main) {
            startActivity(new Intent(MainMensalActivity.this, MensalActivity.class));
            finish();
        }
        else if (id == R.id.diario) {
            startActivity(new Intent(MainMensalActivity.this, DiarioActivity.class));
            finish();
        }
        else if (id == R.id.relatorioDiario) {
            startActivity(new Intent(MainMensalActivity.this, MainActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void deslogarUsuario() {
        try {
            autenticacao.signOut();
            Intent in = new Intent(MainMensalActivity.this, LoginActivity.class);
            startActivity(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }







}
