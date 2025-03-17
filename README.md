# Inception of AWS

![INCEPTION TO AWS](https://user-images.githubusercontent.com/78852244/216152568-0bd33ef1-7f8d-4b8a-99f4-f0e86cb24c03.jpg)

## Prerequisites

- AWS account with EC2, IAM, and S3 permissions
- SSH access to EC2 instances
- Jenkins installed on an Ubuntu server

---

## 1. Create Jenkins Instance on AWS EC2

1. **Create EC2 instance:**
   - Choose the appropriate Ubuntu image (e.g., Ubuntu 20.04).
   - Select an existing SSH key pair (*.pem) during the instance setup.

2. **Install Jenkins:**
   - Update the system:  
     `sudo apt-get update`
   - Install Java:  
     `sudo apt install openjdk-11-jre`
   - Install Jenkins:  
     `sudo apt install jenkins`
   - Install Git:  
     `sudo apt install git-all`
   - Check if Jenkins is running:  
     `sudo service jenkins status`

3. **Open necessary ports (8080):**
   - Go to **Security Groups** in AWS.
   - Edit inbound rules and add a rule for Custom TCP on port `8080`.

---

## 2. Create Website Instance on AWS EC2

1. **Create a new EC2 instance for the website:**
   - Use the previously created SSH key pair (*.pem).
   - Select the appropriate VPC and subnet.
   - Open port `443` (HTTPS) in the Security Group.

---

## 3. Create MariaDB Instance on AWS EC2

1. **Launch an EC2 instance for MariaDB:**
   - Select the same SSH key pair (*.pem) used in previous instances.
   - Open port `3306` for MariaDB connections in the Security Group.

---

## 4. Create AWS S3 Bucket

1. **Create an S3 Bucket** for storing backups and data.

---

## 5. Create IAM Role

1. **Create IAM user and access keys:**
   - Create an Access Key and Secret Access Key.
   - Create IAM Policies with JSON:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "s3:GetBucketLocation",
        "s3:ListAllMyBuckets"
      ],
      "Resource": "arn:aws:s3:::*"
    },
    {
      "Effect": "Allow",
      "Action": "s3:*",
      "Resource": "arn:aws:s3:::BUCKETNAME/*"
    }
  ]
}
```

2. **Create User Groups** and attach IAM policies.

---

## 6. Configure Jenkins and Git Integration

1. **Set permissions for the key pair:**
   - Change the permissions for the SSH key pair file:  
     `sudo chmod 600 *.pem`

2. **Send the Jenkins instance file:**
   - `scp -i ~/.pem /pem user@ipaddress:/tmp/`

3. **Change file owner:**
   - `sudo chown user:user *.pem`

4. **Set up Git connection in Jenkins.**

---

## 7. Jenkins Pipeline Setup

1. **Create `environment.groovy`:**
   - This pipeline will set up the environment for deploying the app.

2. **Create `build_and_deploy.groovy`:**
   - This pipeline compiles and deploys services such as MariaDB, Nginx, WordPress, and phpMyAdmin.

3. **Create `backup.database.groovy`:**
   - This pipeline creates a backup of the database and stores it in S3.

---

## Notes

1. **Add a pipeline to check the health of the instances in the APP:**
   - In case of failure, add monitoring to the website instances.
   - Perform database backups, restore data from backups, and migrate the data if needed.

2. **Monitor database health:**
   - Add steps to monitor the database's health, check backups, and restore as necessary.



