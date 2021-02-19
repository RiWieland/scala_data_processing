Object cosmos_connector{


val configMap = Map(
        "Endpoint" -> xxx,
        "Masterkey" -> xxx,
        "Database" -> xxx,
        "Collection" -> xxx,
        "Upsert" -> xxx
    )

(df.write
  .mode("overwrite")
  .format("com.microsoft.azure.cosmosdb.spark")
  //.format("org.apache.spark.sql.spark")
  .options(configMap)
  .save())


}
