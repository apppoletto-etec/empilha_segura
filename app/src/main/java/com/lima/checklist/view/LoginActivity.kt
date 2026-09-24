package com.lima.checklist.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.lima.checklist.databinding.ActivityLoginBinding
import com.lima.epimanager.helper.exibirMensagem

class LoginActivity : AppCompatActivity() {

    private lateinit var nome: String
    private lateinit var email: String
    private lateinit var senha: String

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this) // Coloque isso bem no início do onCreate
        val firebaseAuth = FirebaseAuth.getInstance()


        setContentView(binding.root)


        inicializarEventosClique()
    }

    override fun onStart() {
        super.onStart()
        verificarUsuarioLogado()
    }

    private fun verificarUsuarioLogado() {
        val usuarioAtual = firebaseAuth.currentUser

        if (usuarioAtual != null) {
            startActivity(
                Intent(this, DiarioActivity::class.java)
            )
        }
    }

    private fun inicializarEventosClique() {
        binding.txtCadastrse.setOnClickListener {
            startActivity(
                Intent(this, CadastroUsuarioActivity::class.java)
            )
        }
        binding.btnLogar.setOnClickListener {
            if (validarCampos()) {
                logarUsuario()
            }
        }
    }

    private fun logarUsuario() {
        firebaseAuth.signInWithEmailAndPassword(email, senha)
            .addOnSuccessListener {
                exibirMensagem("Bem vindo")
                startActivity(
                    Intent(this, DiarioActivity::class.java)
                )
            }
            .addOnFailureListener { erro ->
                try {
                    throw erro
                } catch (erroUsuarioInvalido: FirebaseAuthInvalidUserException) {
                    exibirMensagem("E-mail não cadastrado")
                } catch (erroCredenciaisInvalidas: FirebaseAuthInvalidCredentialsException) {
                    exibirMensagem("E-mail ou senha incorretos")
                }
            }
    }

    private fun validarCampos(): Boolean {
        email = binding.editLoginEmail.text.toString()
        senha = binding.editLoginSenha.text.toString()

        if (email.isNotEmpty()) {
            binding.textLayoutEmailLogin.error = null
            if (senha.isNotEmpty()) {
                binding.textLayoutSenhaLogin.error = null
                return true
            } else {
                binding.textLayoutSenhaLogin.error = "Preencha a sua senha"
                return false
            }
        } else {
            binding.textLayoutEmailLogin.error = "Preencha o seu e-mail"
            return false
        }
    }
}
