# JavaDB操作コマンド

## 環境変数の定義
```
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_271.jdk/Contents/Home
export GLASSFISH_HOME=~/glassfish-5.1.0
export DERBY_HOME=${GLASSFISH_HOME}/javadb
PATH=${JAVA_HOME}/bin:${DERBY_HOME}/bin:${PATH}
```

## 起動と停止
### 起動
```
cd <DBのデータファイルを作成してもよい/作成したディレクトリ>
startNetworkServer
```
### 停止
```
stopNetworkServer
```

## sqlplus相当のツールの起動、停止
### 起動
```
ij
```
### 停止
```
exit;
```

## sqlplus相当のツールij起動下で実施する操作
### データベースの新規作成
```
CONNECT 'jdbc:derby://localhost:1527/jsfdemodb;create=true';
```

### 既存のデータベースへの接続
```
CONNECT 'jdbc:derby://localhost:1527/jsfdemodb';
```

### sqlスクリプトファイルの実行
```
RUN '<sqlスクリプトファイル名>'
```
#### APPUSERテーブルの作成
```
RUN 'APPUSER.sql'
```
#### APPGROUPテーブルの作成
```
RUN 'APPGROUP.sql'
```
#### NEWLY_CONFIRMED_CASES_DAILYテーブルの作成
```
RUN 'NEWLY_CONFIRMED_CASES_DAILY.sql.sql'
```

### csvファイルによるデータロード
```
CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE( null,'<テーブル名>','<csvファイル名>',null,null,null,0);
```
#### APPUSERテーブルのサンプルデータのロード
```
CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE( null,'APPUSER','APPUSER_DATA.csv',null,null,null,0);
```
#### APPGROUPテーブルのサンプルデータのロード
```
CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE( null,'APPGROUP','APPGROUP_DATA.csv',null,null,null,0);
```
#### NEWLY_CONFIRMED_CASES_DAILYテーブルのサンプルデータのロード
```
CALL SYSCS_UTIL.SYSCS_IMPORT_TABLE( null,'NEWLY_CONFIRMED_CASES_DAILY','newly_confirmed_cases_daily2.csv',null,null,null,0);
```
