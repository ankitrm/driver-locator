scalastyleConfig in ThisBuild                                   := file("scalastyle-config.xml")
(scalastyleConfig in Test) in ThisBuild                         := file("test-scalastyle-config.xml")
(scalastyleConfig in IntegrationTest)  in ThisBuild             := (scalastyleConfig in Test).value
(scalastyleConfigUrl in IntegrationTest) in ThisBuild           := (scalastyleConfigUrl in Test).value
(scalastyleConfigRefreshHours in IntegrationTest) in ThisBuild  := (scalastyleConfigRefreshHours in Test).value
(scalastyleTarget in IntegrationTest) in ThisBuild              := target.value / "scalastyle-it-result.xml"
(scalastyleFailOnError in IntegrationTest) in ThisBuild         := (scalastyleFailOnError in Test).value
(scalastyleSources in IntegrationTest) in ThisBuild             := Seq((scalaSource in IntegrationTest).value)
