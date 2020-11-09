package com.sample.app

import org.scalatra._
//import scalalikejdbc._


class MyScalatraServlet extends ScalatraServlet
{
    get("/api/get/:id")
    {
        <h1>$id</h1>
    }
    put("/api/create/:id")
    {
    
    }
    put("/api/update/:id")
    {
    
    }
    put("/api/delete/:id")
    {
    
    }
    
}
