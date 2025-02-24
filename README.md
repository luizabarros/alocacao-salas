# alocacao-salas
Sistemas de alocação de salas.

# Passo a passo para executar a aplicação
1. Defina suas informações no .env <br/>
Você ira encontar um arquivo .env.example com as chaves que você irá precisar usar no sistema. Deve ser criado um arquivo chamado .env com essas mesmas chaves em conjunto com seus respectivos valores.  <br/>
![image](https://github.com/user-attachments/assets/3840bdff-f395-4be5-828f-26e4d7c8f864)  <br/>

2. Defina suas informações no application properties <br/>

3. Para executar o container use o comando: docker-compose up -d <br/>

4. Conecte ao banco de dados depois que o container for construído. Exemplo: <br/>
Acesso ao POSTGRES usando Dbeaver: <br/>
![image](https://github.com/user-attachments/assets/369b4414-9c68-4904-a68d-48f015fe0dff)

# Banco de Dados - Modelagem <br/>
![image](https://github.com/user-attachments/assets/380cafc0-a16f-4c78-adae-b1f971e1308d)

# Acesso o Swagger <br/>
http://localhost:8080/swagger-ui/index.html
