package com.group.quasi.repository

import com.github.tminglei.slickpg._
import io.circe.Json


trait PostgresProfile extends ExPostgresProfile
  with PgArraySupport
  with PgDate2Support
  with PgRangeSupport
  with PgHStoreSupport
  with PgCirceJsonSupport
  with PgSearchSupport
  with PgNetSupport
  with PgLTreeSupport {
  def pgjson = "jsonb" // jsonb support is in postgres 9.4.0 onward; for 9.3.x use "json"

  // Add back `capabilities.insertOrUpdate` to enable native `upsert` support; for postgres 9.5+
  override protected def computeCapabilities: Set[slick.basic.Capability] =
    super.computeCapabilities + slick.jdbc.JdbcCapabilities.insertOrUpdate

  override val api = CustomizedAPI

  object CustomizedAPI extends API with ArrayImplicits
    with DateTimeImplicits
    with JsonImplicits
    with NetImplicits
    with LTreeImplicits
    with RangeImplicits
    with HStoreImplicits
    with SearchImplicits
    with SearchAssistants {
    implicit val strListTypeMapper = new SimpleArrayJdbcType[String]("text").to(_.toList)
    implicit def circeJsonArrayTypeMapper =
      new AdvancedArrayJdbcType[Json](pgjson,
        (s) => io.circe.parser.parse(s).toSeq.flatMap(_.asArray).flatten,
        (v) => v.map(_.noSpaces).mkString("[",",","]")
      ).to(_.toList)
  }
}

object PostgresProfile extends PostgresProfile