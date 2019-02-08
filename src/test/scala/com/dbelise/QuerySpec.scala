package com.dbelise

import com.dbelise.Models.Contact
import org.scalatest.{AsyncFlatSpec, FlatSpec, Matchers}

object Models {
  case class Contact(id: String, name: String, surname: String)
}

class QuerySpec extends AsyncFlatSpec with Matchers {
  val db =
    DB("jdbc:hsqldb:testdb", "sa", "")


  "Database" should "select" in {
    db.model[Contact].findAll
      .map(res => assert(res.size > 0) )
  }

}
