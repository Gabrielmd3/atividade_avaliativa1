package com.example.atividade_avaliativa1.model

class Produto(val nome: String, var quantidade: Int, val categoria: String, val preco: Int) {

    override fun toString(): String {
        return "Produto = $nome\nQuantidade = $quantidade\nCategoria = $categoria\nPreço = $preco"
    }
}