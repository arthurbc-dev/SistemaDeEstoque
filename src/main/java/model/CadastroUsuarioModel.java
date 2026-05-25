package model;

public class CadastroUsuarioModel {

    private int id;
    private String nome;
    private String sobrenome;
    private String matricula;
    private String cpf;
    private String sexo;
    private String dtaNascimento;
    private String email;
    private String telefone;
    private String cep;
    private String endereco;
    private String estado;
    private String bairro;
    private String cidade;
    private String numero;
    private String complemento;
    private String nomeUsuario;
    private String senha;
    private String funcao;

    public CadastroUsuarioModel() {}

    public String getNome() { return this.nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSobrenome() { return this.sobrenome; }
    public void setSobrenome(String sobrenome) { this.sobrenome = sobrenome; }

    public String getCpf() { return this.cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getCep() { return this.cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getEndereco() { return this.endereco; }
    // CORRIGIDO: parâmetro era "nome" e nunca atribuía o valor
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getEstado() { return this.estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getBairro() { return this.bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getCidade() { return this.cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getNumero() { return this.numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getComplemento() { return this.complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public String getNomeUsuario() { return this.nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public String getSenha() { return this.senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getFuncao() { return this.funcao; }
    public void setFuncao(String funcao) { this.funcao = funcao; }

    // CORRIGIDO: getter não faz mais atribuição indevida
    public String getMatricula() { return this.matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public String getSexo() { return this.sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getDtaNascimento() { return this.dtaNascimento; }
    public void setDtaNascimento(String dtaNascimento) { this.dtaNascimento = dtaNascimento; }

    public String getEmail() { return this.email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return this.telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}
