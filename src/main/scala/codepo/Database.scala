package codepo

import java.util.zip.{ZipEntry, ZipFile}
import java.io._
import au.com.bytecode.opencsv.CSVReader
import scala.collection.JavaConversions._


class CodePoDb {

  def lookup(postcode: String) : Option[CodePo] = {
    val source = new ZipFile("/Users/richard/Developer/mini/codepo/codepo_gb.zip")
    CSVs(source).find(row => row(0) == postcode).map(row2codepo).filter(hasLocation_?)
  }

  // Positional Quality flag is documented in code-point-user-guide, although it says "9"
  // for "no location", but the data has "90".
  def hasLocation_?(cp: CodePo) : Boolean = cp.pq != 90

  // Basic unpacking of a Zip file as a stream of names/InputStream pairs --------------

  class ZipElement(val name: String, is: => InputStream) {
    lazy val in = is
  }

  private def unzip(zip: ZipFile) : Stream[ZipElement] = {

    def unzip(zes: java.util.Enumeration[_ <: ZipEntry]) : Stream[ZipElement] =
     zes.hasMoreElements() match {
       case true =>
         val ze = zes.nextElement()
         new ZipElement(ze.getName, zip.getInputStream(ze)) #:: unzip(zes)
       case false => Stream.empty
    }

    unzip(zip.entries)
  }

  // Read ZIP file contents as rows of data -------------------------------------------

  // Data files look like: Data/CSV/ab.csv
  def data_?(e: ZipElement) : Boolean =
    (e.name endsWith ".csv") && (e.name contains "CSV")

  type Row = Seq[String]

  // Find all the CSVs in the file and read them as rows of values
  // TODO: is the zip file content character encoding specified anywhere?
  def CSVs(zip: ZipFile) : Stream[Row] =
    for {
      entry <- unzip(zip).filter(data_?)
      reader = new CSVReader(new InputStreamReader(entry.in, "ASCII"))
      row <- reader.readAll
    } yield row.toSeq


  private def row2codepo(row: Row): CodePo =
    CodePo(
      easting = row(2).toInt,
      northing = row(3).toInt,
      pq = row(1).toInt
    )


}
