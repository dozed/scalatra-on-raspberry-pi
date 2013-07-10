//package org.dots42.rascal
//
//import org.scalatra.test.specs2._
//
//// For more on Specs2, see http://etorreborre.github.com/specs2/guide/org.specs2.guide.QuickStart.html
//class RascalServletSpec extends ScalatraSpec { def is =
//  "GET / on RascalServlet"                     ^
//    "should return status 200"                  ! root200^
//                                                end
//
//  addServlet(classOf[RascalServlet], "/*")
//
//  def root200 = get("/") {
//    status must_== 200
//  }
//}
