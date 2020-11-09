package com.sample.app

import java.sql.DriverManager
import java.sql.Connection
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.sql.DriverManager

import org.scalatra._
import scala.collection.mutable.ListBuffer
//import com.sample.app.Schemas.usersTable.usersTable

//// JSON-related libraries
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.json._


case class usersTable(id: Int, fname: String, lname: String, age: Int, dob: String)

class MyScalatraServlet extends ScalatraServlet with JacksonJsonSupport{
    implicit lazy val jsonFormats: Formats = DefaultFormats
    before() {
        contentType = formats("json")
    }
    try {
        Class.forName("com.mysql.jdbc.Driver")
        val myConn = DriverManager.getConnection("JDBC:mysql://localhost:3306/sample_db", "ayush", "password")
        val myStmt = myConn.createStatement
    
    
        get("/api/getUsers/") {
            val users = myStmt.executeQuery("Select * from users")
            var usersArr: ListBuffer[usersTable] = new ListBuffer[_root_.com.sample.app.usersTable]
            
            while ( {
                users.next
            }) {
                var temp: usersTable = usersTable(id = users.getString("id").toInt, fname = users.getString("fname"), lname = users.getString("lname"), age = users.getString("age").toInt, dob = users.getString("dob").toString)
                usersArr+=temp
            }
        
            for (x <- usersArr) {
                println(x.id, x.fname, x.lname, x.age, x.dob);
            }
            usersArr
        }
        put("/api/create/:id") {
        
        }
        put("/api/update/:id") {
        
        }
        put("/api/delete/:id") {
        
        }
    }
    finally
        {
            println("ERROR")
        }
    
}
