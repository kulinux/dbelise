package com.dbelise

import java.sql.Connection

import scala.concurrent.{ExecutionContext, Future}

import scala.reflect.runtime.universe._


class SqlModel[T: TypeTag] {

  def sqlType(t: Symbol): String = {
    val cls = t.typeSignature.typeSymbol.asClass
    if( t.typeSignature == typeOf[String]) {
      "VARCHAR(255)"
    } else if( t.typeSignature == typeOf[Int]) {
      "INT"
    } else {
      "VARCHAR(255)"
    }
  }


  def sqlCreate(): String = {
    implicit val m = runtimeMirror(getClass.getClassLoader)
    val tpe = m.typeOf[T]
    val attr = tpe
      .members
      .filter(!_.isMethod)

    val attrSql = attr
      .toList
      .sortBy(_.name.toString)
      .map( smb => smb.name + " " + sqlType(smb))
      .mkString(",\n")

    s"""
      |CREATE TABLE ${tpe.typeSymbol.name} (
      |$attrSql
      |)
    """.stripMargin
  }


}

class Model[T: TypeTag](db: DB)(implicit val ec: ExecutionContext) {
  def findAll(): Future[Seq[T]] = ???
  def create(): Future[Unit] = {
    val sql = new SqlModel[T]
    Future {
      val st =
        this.db.con.createStatement()
      st.execute(sql.sqlCreate())
      st.close()
    }
  }

}

class DB(_con: Connection)(implicit val ec: ExecutionContext) {
  def model[T: TypeTag](): Model[T] = new Model[T](this)
  val con = _con
}

object DB {

  def apply(jdbc: String, usr: String, pwd: String,
            ec: ExecutionContext): DB = {
    import java.sql.DriverManager
    Class.forName("org.hsqldb.jdbcDriver")
    val con =
      DriverManager.getConnection(jdbc, usr, pwd)
    new DB(con)(ec)
  }
}
