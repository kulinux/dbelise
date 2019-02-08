package com.dbelise

import java.sql.Connection

import scala.concurrent.Future

class Model[T] {
  def findAll(): Future[Seq[T]] = ???
}

class DB(con: Connection) {
  def model[T](): Model[T] = new Model[T]()
}

object DB {

  def apply(jdbc: String, usr: String, pwd: String): DB = {
    import java.sql.DriverManager
    Class.forName("org.hsqldb.jdbcDriver")
    val con =
      DriverManager.getConnection(jdbc, usr, pwd)
    new DB(con)
  }
}
