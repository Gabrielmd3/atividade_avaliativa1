package com.example.atividade_avaliativa1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.atividade_avaliativa1.model.Produto
import com.example.atividade_avaliativa1.model.Estoque
import com.example.atividade_avaliativa1.ui.theme.Atividade_avaliativa1Theme
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Atividade_avaliativa1Theme {
                LayoutProduto()
            }
        }
    }
}

@Composable
fun LayoutProduto() {
    val navController = rememberNavController()

    // NavHost define as rotas e o ponto de entrada
    NavHost(navController = navController, startDestination = "addestoque") {
        composable("addestoque") { AddProduto(navController) }
        composable("detalheproduto/{produtoJson}") { backStackEntry ->
            val produtoJson = backStackEntry.arguments?.getString("produtoJson")
            if (produtoJson != null) {
                val produtoJ = Gson().fromJson(produtoJson, Produto::class.java)
                DetalheProduto(navController, produtoJ)
            }
        }
        composable("listaprodutos") {ListaProdutos(navController) }

        composable("estatisticaprodutos/{valortotal}/{quantidadetotal}") { backStackEntry ->
            val quantidadetotal = backStackEntry.arguments?.getString("quantidadetotal")?.toIntOrNull()
            val valortotal = backStackEntry.arguments?.getString("valortotal")?.toIntOrNull()
            if (quantidadetotal != null && valortotal != null) {
                EstatisticaProdutos(navController, quantidadetotal, valortotal)
            }
        }
    }
}

@Composable
fun AddProduto(navController: NavHostController) {
    var nome by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)) {
        // Campo de texto para nome
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .height(70.dp),
            value = nome,
            onValueChange = { nome = it },
            label = { Text(text = "Nome do Produto:") },
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Campo de texto para quantidade
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .height(70.dp),
            value = quantidade,
            onValueChange = { quantidade = it },
            label = { Text(text = "Quantidade: ") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Campo de texto para nível mínimo
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .height(70.dp),
            value = preco,
            onValueChange = { preco = it },
            label = { Text(text = "Preço: ") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(10.dp))

        // Campo de texto para quantidade máxima
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .height(70.dp),
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text(text = "Categoria: ") },
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            onClick = {
                if (quantidade.toInt() <= 0 || preco.toInt() <= 0) {
                    Toast.makeText(context, "Quantidade e preço devem ser maiores que 0", Toast.LENGTH_SHORT).show()
                } else {
                    Estoque.adicionarProduto(
                        Produto(
                            nome = nome,
                            quantidade = quantidade.toInt(),
                            categoria = categoria,
                            preco = preco.toInt()
                        )
                    )
                }
                }
        ) {
            Text(text = "Cadastrar Produto")
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            onClick = {
                navController.navigate("listaprodutos")
            }
        ) {
            Text(text = "Lista de produtos")
        }



    }
}

@Composable
fun ListaProdutos(navController: NavController){
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            val produtos = Estoque.retornarProdutos()
            items(produtos) { produto ->
                Text(text = "${produto.nome} - ${produto.quantidade}", fontSize = 20.sp,
                    modifier = Modifier.clickable {
                        val produtoJson = Gson().toJson(produto)
                        navController.navigate("detalheproduto/$produtoJson")
                    })
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            val valorTotal = Estoque.calcularValorTotalEstoque()
            val quantidadeTotal = Estoque.calcularTotalEstoque()
            navController.navigate("estatisticaprodutos/${valorTotal}/${quantidadeTotal}")
        }) {
            Text("Ver estatisticas")
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Voltar")
        }
    }
}

@Composable
fun EstatisticaProdutos(navController: NavController, quantidadetotal: Int, valortotal: Int){
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)) {

        Text(text = "Valor Total do Estoque: $valortotal")
        Text(text = "Quantidade Total de Produtos: $quantidadetotal")

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Voltar")
        }
    }
}

@Composable
fun DetalheProduto(navController: NavController, produto: Produto){
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment =  Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text("Nome: ${produto.nome}\nQuantidade: ${produto.quantidade}\nPreço: ${produto.preco}\nCategoria: ${produto.categoria}", fontSize = 18.sp)

        Button(onClick = {
            navController.popBackStack() // Volta para Tela1
        }) {
            Text("Voltar")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LayoutProduto()
}