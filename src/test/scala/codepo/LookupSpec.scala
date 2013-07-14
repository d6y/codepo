package codepo

import org.specs2.mutable._

class LookupSpec extends Specification {

  "Location lookup" should {

    val db = new CodePoDb
    val bn13eh = (db lookup "BN1 3EH")

    "find BN1 3EH at 530510 104451" in {
      bn13eh must beSome(CodePo(easting=530510,northing=104451,pq=10))
    }

    "result can compute distance" in {
      val nearby = CodePo(530262,104411,10)
      bn13eh.map(_.distanceTo(nearby)) must beSome(251)
      bn13eh.map(nearby.distanceTo) must beSome(251)
    }

  }

}
