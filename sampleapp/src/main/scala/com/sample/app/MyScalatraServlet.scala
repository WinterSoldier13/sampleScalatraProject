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
case class usersTable(id: Int, fname: String, lname: String, age: Int, dob: String)



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
        get("/api/getUsers/") {
            val users = myStmt.executeQuery("Select * from users")
            var usersArr: ListBuffer[usersTable] = new ListBuffer[_root_.com.sample.app.usersTable]
            
            while ( {
                users.next
            }) {
                var temp: usersTable = usersTable(id = users.getString("id").toInt, fname = users.getString("fname"), lname = users.getString("lname"), age = users.getString("age").toInt, dob = users.getString("dob").toString)
                usersArr += temp
            }
            
            for (x <- usersArr) {
                println(x.id, x.fname, x.lname, x.age, x.dob);
            }
//            return all Users in JSON format
            usersArr
        }
        
//        Add a new user to the database
        put("/api/create/") {
            //            GET THE JSON HERE
            var a = parsedBody.extract[usersTable]
            
            val fname = a.fname.toString
            val lname = a.lname.toString
            val age: Int = a.age.toInt
            val dob = a.dob.toString
            
            println(s"RECEIVED $fname $lname $age $dob")
            var command: String = s"INSERT INTO users(fname, lname, age, dob) VALUES ( '$fname' , '$lname' , $age , '$dob')";
            myStmt.executeUpdate(command)
            
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
                val temp: usersTable = usersTable(id = id_, fname = users.getString("fname"), lname = users.getString("lname"), age = users.getString("age").toInt, dob = users.getString("dob").toString)
                 initFname = temp.fname.toString;
                 initLname = temp.lname.toString;
                 initAge = temp.age.toInt;
                 initDOB = temp.dob.toString;
            }
            println(s"Previously Stored $initFname $initLname $initAge $initDOB")
            
            
//            TIME TO UPDATE
            var receivedJSON = parsedBody.extract[usersTable]
            
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
            
            var c :String = s"UPDATE users SET fname='$newFname', lname='$newLname', age=$newAge, dob='$newDOB' WHERE id=$id_"
            myStmt.executeUpdate(c)
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
