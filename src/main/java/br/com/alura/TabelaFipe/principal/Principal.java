package br.com.alura.TabelaFipe.principal;

import br.com.alura.TabelaFipe.model.Dados;
import br.com.alura.TabelaFipe.model.Modelos;
import br.com.alura.TabelaFipe.model.Veiculo;
import br.com.alura.TabelaFipe.service.ConsumoApi;
import br.com.alura.TabelaFipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    private String endereco;
    private ConverteDados conversor = new ConverteDados();

    public void exibeMenu() {
        String menu = """
        ===============================
               *** OPÇÕES ***
               Carro
               Moto
               Caminhão
        ===============================
        """;

        System.out.println(menu);
        System.out.print("Insira a opção desejada: ");
        String opcao = scanner.nextLine();

        if (opcao.toLowerCase().contains("carr")) {
            endereco = URL_BASE + "carros/marcas";
        } else if (opcao.toLowerCase().contains("mot")) {
            endereco = URL_BASE + "motos/marcas";
        } else if (opcao.toLowerCase().contains("caminh")) {
            endereco = URL_BASE + "caminhoes/marcas";
        } else {
            System.out.println("Opção não encontrada");
        }

        var json = consumoApi.obterDados(endereco);
        List<Dados> marcas = conversor.obterLista(json, Dados.class);

        System.out.println("\n===== MARCAS DISPONÍVEIS =====");
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.print("\nInforme o código da marca para consulta: ");
        var codigoMarca = scanner.nextLine();

        endereco += "/" + codigoMarca + "/modelos";
        json = consumoApi.obterDados(endereco);

        Modelos modeloLista = conversor.obterDados(json, Modelos.class);
        System.out.println("\n===== MODELOS DISPONÍVEIS =====");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.print("\nInsira o nome do veículo que deseja buscar: ");
        var nomeVeiculo = scanner.nextLine();

        List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\n===== MODELOS FILTRADOS =====");
        modelosFiltrados.forEach(System.out::println);

        System.out.print("\nDigite o código do modelo: ");
        var codigoModelo = scanner.nextLine();

        endereco += "/" + codigoModelo + "/anos";
        json = consumoApi.obterDados(endereco);
        List<Dados> anos = conversor.obterLista(json, Dados.class);

        List<Veiculo> veiculos = new ArrayList<>();
        for (Dados ano : anos) {
            var enderecoAnos = endereco + "/" + ano.codigo();
            json = consumoApi.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("\n===== VEÍCULOS FILTRADOS POR ANO =====");
        veiculos.forEach(System.out::println);
    }
}