package com.group.quasi.domain.infra

package object storage {
  sealed trait StorageOption
  sealed trait StorageConfig
  case object FTP extends StorageOption
  case object AwsS3 extends StorageOption
  case object LocalStorage extends StorageOption
  final case class FtpConfig(host: String, port: Int, userName: String, password: String, destination: String)
      extends StorageConfig
  final case class S3config(bucket: String) extends StorageConfig
  final case class LocalStorageConfig(path: String) extends StorageConfig
  final case class StorageConfigs(currentOption: StorageOption, configs: Map[StorageOption, StorageConfig])
  object StorageOption {
    def unsafe(value: String): StorageOption = value.toLowerCase match {
      case "local" => LocalStorage
      case "ftp" => FTP
      case "awss3" => AwsS3
      case _ => throw new IllegalArgumentException(s"$value is not supported StorageOption type")
    }
  }
}
