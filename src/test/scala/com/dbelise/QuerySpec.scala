package com.dbelise

import com.dbelise.Models.Contact
import org.scalatest.{AsyncFlatSpec, FlatSpec, Matchers}
import org.scalactic.Equality

import scala.concurrent.ExecutionContext

object Models {
  case class Contact(id: Int, name: String, surName: String)
}

object Equalities {
  implicit val similarStr = new Equality[String] {

    def removeNoise(a: String) =
      a.toUpperCase()
      .replace('\n', ' ')
      .replaceAll("[ \t]+", " ")
      .trim()

    override def areEqual(a: String, b: Any): Boolean = {
      removeNoise(a) == removeNoise(b.asInstanceOf[String])
    }
  }
}


class SqlModelSpec extends FlatSpec with Matchers {
  val model = new SqlModel[Contact]
  "SqlModel" should "create sql" in {
    model.sqlCreate() should equal (
      """
        |CREATE TABLE Contact (
        |id INT,
        |name VARCHAR(255),
        |surname VARCHAR(255)
        |)
      """.stripMargin
    )(Equalities.similarStr)
  }

}

class ModelSpecAsync extends AsyncFlatSpec with Matchers {
  val model =
    DB("jdbc:hsqldb:testdb", "sa", "",
      implicitly[ExecutionContext]).model[Contact]


  "Database" should "create" in {
   model
     .create()
     .map(res => assert(true) )
  }

  "Database" should "select" in {
    model.findAll
      .map(res => assert(res.size > 0) )
  }

}
