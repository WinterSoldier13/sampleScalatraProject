package com.sample.app

import java.sql.DriverManager
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.sql.DriverManager

import org.scalatra._
import scala.collection.mutable.ListBuffer

//// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._


// Defining my case class here
case class UsersTable(id: Int = -1, fname: String, lname: String, age: Int, dob: String)

import scalikejdbc._


class MyScalatraServlet extends ScalatraServlet with JacksonJsonSupport {
    
    implicit lazy val jsonFormats: Formats = DefaultFormats
    
    before() {
        contentType = formats("json")
    }
    
    
    try {
        Class.forName("com.mysql.jdbc.Driver")
        val myConn = DriverManager.getConnection("JDBC:mysql://localhost:3306/sample_db", "ayush", "password")
        val myStmt = myConn.createStatement
        
//        Get All Users
        get("/api/getUsers/")
        {
            // initialize JDBC driver & connection pool
            Class.forName("com.mysql.jdbc.Driver")
            ConnectionPool.singleton("JDBC:mysql://localhost:3306/sample_db", "ayush", "password")
    
            // ad-hoc session provider on the REPL
            implicit val session = AutoSession
    
            // for now, retrieves all data as Map value
            val entities: List[Map[String, Any]] = sql"select * from users"
              .map(_.toMap)
              .list
              .apply()
//            Converting to right datatype because on directly returning the entities 'dob' is not being returned
            var output : ListBuffer[Map[String, Any]] = new ListBuffer[Map[String, Any]]
            
            for(x <- entities)
                {
                    val temp = Map("dob" -> x("dob").toString, "fname" -> x("fname"), "lname" ->x("lname"), "age" -> x("age"), "id" -> x("id"))
                    output += temp
                }
//          Return the list it will be automatically converted to JSON
            output
        }
        
//        Add a new user to the database
        put("/api/create/") {
            //            GET THE JSON HERE
            var a = parsedBody.extract[UsersTable]
            
            val fname = a.fname.toString
            val lname = a.lname.toString
            val age: Int = a.age.toInt
            val dob = a.dob.toString
            // initialize JDBC driver & connection pool
            Class.forName("com.mysql.jdbc.Driver")
            ConnectionPool.singleton("JDBC:mysql://localhost:3306/sample_db", "ayush", "password")
    
            // ad-hoc session provider on the REPL
            implicit val session = AutoSession
            
            println(s"RECEIVED $fname $lname $age $dob")
            var command = sql"""INSERT INTO users(fname, lname, age, dob) VALUES ( ${fname} , ${lname} , ${age} , ${dob})"""
              .update
              .apply()
            
            //            println(command)
            println("Added to Table")
        }
        
//        update an existing user
        put("/api/update/:id") {
            val id_ = params {
                "id"
            }.toInt
            println(s"ID RECEIVED : $id_")
            
            //            First I'll get the previous data stored in the DB
            val command: String = s"SELECT * FROM users WHERE id=$id_"
            val users = myStmt.executeQuery(command)
            var initFname :String = "";
            var initLname : String = "";
            var initAge : Int = -1;
            var initDOB : String = "";
            
            while ( {
                users.next()
            }) {
                val temp: UsersTable = UsersTable(id = id_, fname = users.getString("fname"), lname = users.getString("lname"), age = users.getString("age").toInt, dob = users.getString("dob").toString)
                 initFname = temp.fname.toString;
                 initLname = temp.lname.toString;
                 initAge = temp.age.toInt;
                 initDOB = temp.dob.toString;
            }
            println(s"Previously Stored $initFname $initLname $initAge $initDOB")
            
            
//            TIME TO UPDATE
            var receivedJSON = parsedBody.extract[UsersTable]
            
            var newFname = initFname;
            var newLname = initLname;
            var newAge = initAge;
            var newDOB = initDOB;
    
            if(receivedJSON.fname != "")
                {
                    newFname = receivedJSON.fname.toString;
                }
            if(receivedJSON.lname != "")
                {
                    newLname = receivedJSON.lname.toString;
                }
            if(receivedJSON.age != None)
                {
                    newAge = receivedJSON.age.toInt
                }
            if(receivedJSON.dob != "")
                {
                    newDOB = receivedJSON.dob;
                }
    
            // initialize JDBC driver & connection pool
            Class.forName("com.mysql.jdbc.Driver")
            ConnectionPool.singleton("JDBC:mysql://localhost:3306/sample_db", "ayush", "password")
    
            // ad-hoc session provider on the REPL
            implicit val session = AutoSession
            
            var c = sql"""UPDATE users SET fname=${newFname}, lname=${newLname}, age=${newAge}, dob=${newDOB} WHERE id=${id_}"""
              .update
              .apply()
            
            println("UPDATED")
        }
        
//        delete an existing User
        delete("/api/delete/:id")
        {
            val id_ : Int = params{"id"}.toInt
            val command = s"DELETE FROM users WHERE id=$id_"
            myStmt.executeUpdate(command)
            println(command)
        }
    } // try ends here
    finally {
        println("ERROR")
    }
    
}
