packer {
  required_plugins {
    amazon = {
      source  = "github.com/hashicorp/amazon"
      version = ">=1.0.0"
    }
  }
}

variable "source_ami" {
  type    = string
  default = "ami-06db4d78cb1d3bbf9" # debian 64
}
variable "ssh_username" {
  type    = string
  default = "admin"
}
variable "aws_region" {
  type    = string
  default = "us-east-1"
}
source "amazon-ebs" "custom-ami" {
  region     = var.aws_region
  source_ami = var.source_ami
  ami_name   = "csye6225_${formatdate("YYYY_MM_DD_hh_mm_ss", timestamp())}"
  #  description   = "Custom AMI Example CSYE 6225"
  instance_type = "t2.micro" # Specify the instance type for the temporary instance
  ssh_username  = var.ssh_username
  profile       = "dev"
  ami_users     = ["957845414123"]

  aws_polling {
    delay_seconds = 120
    max_attempts  = 50
  }

}

build {

  sources = ["source.amazon-ebs.custom-ami"]

  provisioner "file"{
    source = " ./target/CloudAssignment2-0.0.1-SNAPSHOT.jar"
    destination = "/tmp/CloudAssignment2-0.0.1-SNAPSHOT.jar"
  }
  provisioner "file"{
    source = " ./opt/users.csv"
    destination = "/tmp/users.csv"
  }

  provisioner "shell" {
    environment_vars = [
      "DEBIAN_FRONTEND=noninteractive",
      "CHECKPOINT_DISABLE=1"
    ]
    inline = [
      "sudo apt-get update",
      "sudo apt-get upgrade -y",

      "sudo apt-get clean",
    ]
  }


}
