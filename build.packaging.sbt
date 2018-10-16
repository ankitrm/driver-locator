packageBin in Universal := {
  val originalFileName = (packageBin in Universal).value
  val newFileName = file(originalFileName.getParent) / "current.zip"
  IO.copyFile(originalFileName, newFileName)
  originalFileName
}
