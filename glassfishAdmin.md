# glassfish操作コマンド

## 環境変数の定義
```Shell
export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_271.jdk/Contents/Home
export GLASSFISH_HOME=~/glassfish-5.1.0
PATH=${JAVA_HOME}/bin:${GLASSFISH_HOME}/bin:${PATH}
```

## [サーバの起動と停止](https://glassfish.org/docs/latest/quick-start-guide/basic-features.html#GSQSG00033)
### [起動](https://glassfish.org/docs/latest/reference-manual/start-domain.html)
```Shell
asadmin start-domain
```
### [確認](https://glassfish.org/docs/latest/reference-manual/list-domains.html)
```Shell
asadmin list-domains
```
### [停止](https://glassfish.org/docs/latest/reference-manual/stop-domain.html)
```Shell
asadmin stop-domain
```

## [アプリケーションの配備、確認、削除](https://glassfish.org/docs/latest/quick-start-guide/basic-features.html#GSQSG00036)
### [配備](https://glassfish.org/docs/latest/reference-manual/deploy.html)
```Shell
asadmin deploy --contextroot=jsfdemo --name=jsfdemo ~/Projects/jsfdemo/target/jsfdemo-1.0-SNAPSHOT.war
```
### [確認](https://glassfish.org/docs/latest/reference-manual/list-applications.html)
```Shell
asadmin list-applications
```
### [削除](https://glassfish.org/docs/latest/reference-manual/undeploy.html)
```Shell
asadmin undeploy jsfdemo
```

## [コネクションプールの作成、接続テスト、確認、削除](https://glassfish.org/docs/latest/administration-guide/jdbc.html#GSADG00745)
### [作成](https://glassfish.org/docs/latest/reference-manual/create-jdbc-connection-pool.html)
```Shell
asadmin create-jdbc-connection-pool --datasourceclassname org.apache.derby.jdbc.ClientXADataSource --restype javax.sql.XADataSource --property serverName=localhost:portNumber=1527:databaseName=jsfdemodb:user=APP:password=APP jsfdemodbpool
asadmin create-jdbc-connection-pool --datasourceclassname org.apache.derby.jdbc.BasicClientConnectionPoolDataSource40 --restype javax.sql.ConnectionPoolDataSource --property serverName=localhost:portNumber=1527:databaseName=jsfdemodb:user=APP:password=APP jsfdemodbpool2
```
### [接続テスト](https://glassfish.org/docs/latest/reference-manual/ping-connection-pool.html)
```Shell
asadmin ping-connection-pool jsfdemodbpool
asadmin ping-connection-pool jsfdemodbpool2
```
### [確認](https://glassfish.org/docs/latest/reference-manual/list-jdbc-connection-pools.html)
```Shell
asadmin list-jdbc-connection-pools
```
### [削除](https://glassfish.org/docs/latest/reference-manual/delete-jdbc-connection-pool.html)
```Shell
asadmin delete-jdbc-connection-pool jsfdemodbpool
asadmin delete-jdbc-connection-pool jsfdemodbpool2
```

## [JDBCリソースの作成、確認、削除](https://glassfish.org/docs/latest/administration-guide/jdbc.html#GSADG00746)
### [作成](https://glassfish.org/docs/latest/reference-manual/create-jdbc-resource.html)
```Shell
asadmin create-jdbc-resource --connectionpoolid jsfdemodbpool jdbc/jsfdemodbpool
asadmin create-jdbc-resource --connectionpoolid jsfdemodbpool2 jdbc/jsfdemodbpool2
```
### [確認](https://glassfish.org/docs/latest/reference-manual/list-jdbc-resources.html)
```Shell
asadmin list-jdbc-resources
```
### [削除](https://glassfish.org/docs/latest/reference-manual/delete-jdbc-resource.html)
```Shell
asadmin delete-jdbc-resource jdbc/jsfdemodbpool
asadmin delete-jdbc-resource jdbc/jsfdemodbpool2
```

## [セキュリティレルムの作成、確認、削除](https://glassfish.org/docs/latest/security-guide/user-security.html#GSSCG00036)
### [作成](https://glassfish.org/docs/latest/reference-manual/create-auth-realm.html)
```Shell
asadmin create-auth-realm --classname com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm --property jaas-context=jdbcRealm:datasource-jndi=jdbc/jsfdemodbpool:user-table=APPUSER:user-name-column=ID:password-column=PASSWORD:group-table=APPGROUP:group-table-user-name-column=USERID:group-name-column=NAME:digest-algorithm=SHA-256:encoding=Base64 jsfdemorealm
```
### [確認](https://glassfish.org/docs/latest/reference-manual/list-auth-realms.html)
```Shell
asadmin list-auth-realms
```
### [削除](https://glassfish.org/docs/latest/reference-manual/delete-auth-realm.html)
```Shell
asadmin delete-auth-realm jsfdemorealm
```
