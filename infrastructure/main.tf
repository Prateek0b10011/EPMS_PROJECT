# Configure the AWS Provider
provider "aws" {
  region = "us-east-1"
}

# Create a Security Group to allow access
resource "aws_security_group" "epms_sg" {
  name        = "epms-security-group"
  description = "Allow inbound traffic for MySQL and SSH"

  # SSH Access
  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # MySQL Database Access (for EPMS Database)
  ingress {
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  # Outbound rules (allow all internet traffic)
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# Request a Free-Tier EC2 instance
resource "aws_instance" "epms_db_server" {
  ami           = "ami-0c7217cdde317cfec" # Ubuntu 22.04 LTS in us-east-1
  instance_type = "t2.micro"             # Eligible for AWS Free Tier

  security_groups = [aws_security_group.epms_sg.name]

  # User data to automatically install Docker on start-up
  user_data = <<-EOF
              #!/bin/bash
              # Update packages
              sudo apt-get update -y
              
              # Install Docker dependencies
              sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common
              
              # Add Docker official GPG key
              curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
              
              # Set up stable Docker repository
              echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
              
              # Install Docker
              sudo apt-get update -y
              sudo apt-get install -y docker-ce docker-ce-cli containerd.io
              
              # Start and enable Docker service
              sudo systemctl start docker
              sudo systemctl enable docker
              
              # Add default ubuntu user to docker group
              sudo usermod -aG docker ubuntu
              EOF

  tags = {
    Name = "EPMS-Database-Server"
  }
}

# Output the public IP of the server once created
output "server_public_ip" {
  value       = aws_instance.epms_db_server.public_ip
  description = "The public IP of the newly created database server"
}
