package object codepo {

// class Easting(val value: String) extends AnyRef
// class Northing(val value: String) extends AnyRef
// class PQ(val value: String) extends AnyRef

  // 6 digits in Eastings (or Northings) means 1 meter resolution.
  // PQ is positional quality, defined in code point user guide.
  case class CodePo(easting: Int, northing: Int, pq: Int) {

    type Meter = Int
    def distanceTo(other: CodePo) : Meter =
      math.sqrt( math.pow((other.easting - this.easting),2) + math.pow((other.northing - this.northing),2) ).round.toInt

  }

}
