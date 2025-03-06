# alocacao-salas
Sistemas de alocação de salas.

# Passo a passo para executar a aplicação
1. Defina suas informações no .env <br/>
Você ira encontar um arquivo .env.example com as chaves que você irá precisar usar no sistema. Deve ser criado um arquivo chamado .env com essas mesmas chaves em conjunto com seus respectivos valores.  <br/>
![image](https://github.com/user-attachments/assets/3840bdff-f395-4be5-828f-26e4d7c8f864)  <br/>

2. Defina suas informações no application properties <br/>
Em caso de erro na chave do sendgrid, é recomendado criar outra na plataforma do sendgrid. Adicione no application properties. <br/>

3. Para executar o container use o comando: docker-compose up -d <br/>

4. Conecte ao banco de dados depois que o container for construído. Exemplo: <br/>
Acesso ao POSTGRES usando Dbeaver: <br/>
![image](https://github.com/user-attachments/assets/369b4414-9c68-4904-a68d-48f015fe0dff)

# Banco de Dados - Modelagem <br/>
![image](https://github.com/user-attachments/assets/ef7f6792-bb7c-4a8f-82d2-18d4a36caf1f)

# Acesso o Swagger <br/>
http://localhost:8080/swagger-ui/index.html

# Login padrão (admin): <br/>
20231160027@ifba.edu.br
Admin@123
