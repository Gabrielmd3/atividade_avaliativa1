package com.example.atividade_avaliativa1.model

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.google.gson.Gson

class Estoque {
    companion object {
        private val listaProdutos = mutableListOf<Produto>()

        fun adicionarProduto(produto: Produto) {
            listaProdutos.add(produto)
        }

        fun calcularValorTotalEstoque(): Int {
            return listaProdutos.sumOf { it.preco * it.quantidade }
        }

        fun calcularTotalEstoque(): Int {
            return listaProdutos.sumOf {it.quantidade }
        }

        fun retornarProdutos(): MutableList<Produto> {
            return listaProdutos
        }
    }
}