package com.sample.app

import org.scalatra._
import scalalikejdbc._


class MyScalatraServlet extends ScalatraServlet
{
    get("/api/get/:id")
    {
        views.html.hello()
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
