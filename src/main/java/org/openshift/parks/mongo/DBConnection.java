package org.openshift.parks.mongo;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

@Named
@ApplicationScoped
public class DBConnection {

	private static final String COLLECTION_NAME = "parks";

	private MongoDatabase mongoDB;

	// The name and the path where the VolumeMount placed the properties in the ConfigMap.
	String fileName = "/etc/mongo/atlas-connection.prop";

	@PostConstruct
	public void afterCreate() {

		Properties props = getConnectionProps(fileName);

		String mongoServers = (String) props.get("servers");
		String mongoReplicaSet = (String) props.get("replicaset");
		String mongoUser = (String) props.get("user");
		String mongoPassword = (String) props.get("password");
		String mongoDBName = (String) props.get("database");

		String URIString = new String("mongodb://" + mongoUser + ":" + mongoPassword + "@" + mongoServers);

		MongoClientURI uri = new MongoClientURI(URIString + "/admin?ssl=true&replicaSet=" + mongoReplicaSet+"&authSource=admin");
		try {

			MongoClient mongoClient = new MongoClient(uri);
			mongoDB = mongoClient.getDatabase(mongoDBName);

		} catch (Exception e) {
			e.printStackTrace();
		}
		

		
	}

	private Properties getConnectionProps(String fileName) {

		Properties props = new Properties();
		FileReader fileReader = null;

		try {
			// FileReader reads text files in the default encoding.
			fileReader = new FileReader(fileName);
			props.load(fileReader);
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		}
		catch(IOException ex) {
			System.out.println( "Error reading file '"  + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		} finally {
			try {
				fileReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}



		return props;


	}
	public MongoDatabase getDB() {
		return mongoDB;
	}


	public MongoCollection getCollection() {
		return mongoDB.getCollection(COLLECTION_NAME);
	}





}
