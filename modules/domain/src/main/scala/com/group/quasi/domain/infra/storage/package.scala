package com.group.quasi.domain.infra

package object storage {
  sealed trait StorageOption
  sealed trait StorageConfig
  case object FTP extends StorageOption
  case object AwsS3 extends StorageOption
  case object LocalStorage extends StorageOption
  final case class FtpConfig(host: String, port: Int,
                             userName: String, password:String, destination: String) extends StorageConfig
  final case class S3config(bucket: String)  extends StorageConfig
  final case class LocalStorageConfig(path: String)  extends StorageConfig
  final case class StorageConfigs(currentOption: StorageOption,
                                 configs: Map[StorageOption,StorageConfig] )
}
