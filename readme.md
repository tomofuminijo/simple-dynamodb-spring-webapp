# Spring Boot 上で動作してDynamoDB と連携するシンプルなWeb アプリケーション

Spring Boot 上のシンプルなWebアプリケーションです。AWS SDK for Java を利用してDynamoDB と連携します。   
AWS X-Ray との連携や DynamoDB Local を利用した完全ローカル環境での実行方法も記述します。

# Cloud9 上で動作させる手順

以下、Cloud9 上で動作させる手順です。基本的にはどの環境でも動かせます。（OpenJDK 8 およびMaven があれば良いです。）


# Step1 JDK 11 のインストール
ここでは、[Amazon Corretto 11](https://aws.amazon.com/jp/corretto/)　をインストールします。   
まず、Cloud9 上でターミナルウィンドウを開き、以下のコマンドを実行します。

```
wget https://corretto.aws/downloads/latest/amazon-corretto-11-x64-linux-jdk.rpm

sudo rpm -ihv /home/ec2-user/environment/amazon-corretto-11-x64-linux-jdk.rpm
```

# Step2: Maven のインストール
ターミナル上で以下のコマンドを実行します。

```
sudo wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo
sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo
sudo yum install -y apache-maven
```

- 参考URL : [Maven を使用して設定する](https://docs.aws.amazon.com/ja_jp/cloud9/latest/user-guide/sample-java.html#sample-java-sdk-maven)

# Step3: git clone、ビルド、実行

- build 処理
    ```
    git clone https://github.com/tomofuminijo/simple-dynamodb-spring-webapp.git

    cd simple-dynamodb-spring-webapp
    mvn package 
    ```

- アプリケーション実行
  ```
  java -jar target/my-greeting-web-1.0.0.jar
  ```

- ブラウザからアクセス   
  アプリケーションを実行すると以下のようなメッセージがターミナル右上に表示されます。   
    ```
    Cloud9 Help
    Your code is running at https://xxxxxxxxxxxxx.vfs.cloud9.us-east-1.amazonaws.com
    ```

    表示されたリンクをクリックするとアプリケーションにアクセスできます。

- アプリの動作確認  
    1. まだ必要なテーブルやデータがDynamoDB 上に無いので、まず最初に表示された画面の"Greeting" ボタンの下の **init** リンクをリクックします。  
    何もエラー画面が表示されなければ正常に動作しています。  
    Cloud9 を作成したリージョンのDynamoDB 上に"DevDemoGreeting" というテーブルが作成されて、データが登録されます。

    1. "Language" の入力欄に "ja" や"fr"、"ko" などと入力して"Greeting" ボタンを押すと、それぞれの言語にHello が表示されます。データはDynamoDB　から取得されます。



    アプリはこれだけの非常にシンプルなものです。

# Step4: AWS X-Ray との連携

上記アプリ実行時に、Exception が出力されています。以下のような内容です。

```
com.amazonaws.SdkClientException: Unable to execute HTTP request: Connect to 127.0.0.1:2000 [/127.0.0.1] failed: Connection refused
```

ソースコード上ではX-Ray と連携するServletFilter やDynamoDB Client の設定がされていますが、X-Ray デーモンが動作していないため発生するエラーです。

## X-Ray デーモンの起動方法

以下を実行します。

- X-Ray Daemon インストール
    ```
    cd ~

    wget https://s3.dualstack.us-east-2.amazonaws.com/aws-xray-assets.us-east-2/xray-daemon/aws-xray-daemon-linux-3.x.zip

    unzip -d xray aws-xray-daemon-linux-3.x.zip 
    ```
- X-Ray Daemon のローカル起動
    
    "\<your-reion\>" をCloud9 を実行しているリージョンに変更してください。
    
    ```
    cd ~/xray
    ./xray -o -n <your-region>
    ```

    もしくは以下のように実行します。

    ```
    ./xray -o -n $(curl -s http://169.254.169.254/latest/meta-data/placement/availability-zone | sed -e 's/.$//')
    ```

- X-Ray の動作確認

一旦Java プロセスを終了して再度起動してください。先ほどと同じようにアプリにアクセスしてもException は発生しません。また　xray デーモンのコンソール上に以下のような情報が表示されます。

```
2019-03-19T12:43:23Z [Info] Successfully sent batch of 2 segments (0.027 seconds
```

マネージメントコンソール上でX-Ray サービスにアクセスして情報を確認してみてください。

# Step5: DynamoDB ローカル を利用したローカル実行

ここまでは、アプリケーションは実際のAWS 上のDynamoDB にアクセスしています。開発環境でローカルのみでテストしたい場合は、DynamoDB ローカルを利用できます。

## DynamoDB ローカルの実行

最も簡単なのはDokcer イメージを利用することです。Cloud9 ではデフォルトでDocker Clientが入っています。  
以下を実行するだけでDynamoDB ローカルを実行できます。

```
docker run -p 8000:8000 amazon/dynamodb-local
```

もしくは、Embedded モードで動作もできます。その場合はJava プロセス内に同居して実行されます。以下のURLを参照してください。  

[DynamoDB (ダウンロード可能バージョン) と Apache Maven - Amazon DynamoDB](https://docs.aws.amazon.com/ja_jp/amazondynamodb/latest/developerguide/DynamoDBLocal.Maven.html)

## application.xml の変更

**src > main > resource > config > application.xml** を開きます。

以下のようにlocal を **true** に変更します。region はローカルで動作するので変えなくても問題ありません。

```
amazon:
  dynamodb:
    local: true
    endpoint: http://localhost:8000
    region: us-east-1
  credential:
    profile: default
```

以下のように再コンパイルしてから、起動します。
```
mvn package
java -jar target/my-greeting-web-0.1.0.jar
```

エラーが出ずに動作すればOKです。   
本当にDynamoDB ローカルが利用されているかアプリの動作だけではわかりませんので、以下のコマンドでDynamoDB ローカル内のテーブル一覧を表示してみます。

```
aws dynamodb list-tables --endpoint-url http://localhost:8000 --region us-east-1
```

以下のように表示されます。
```
{
    "TableNames": [
        "DevDemoGreeting"
    ]
}
```

以上です。
