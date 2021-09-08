#!/bin/sh 

awslocal sns create-topic --name sqs-status

awslocal sqs create-queue --queue-name status-queue


awslocal sns subscribe \
  --topic-arn arn:aws:sns:us-east-1:000000000000:sqs-status \
  --protocol sqs \
  --notification-endpoint http://localhost:4576/queue/status-queue


# awslocal sns publish --topic-arn arn:aws:sns:us-east-1:000000000000:sqs-status --message "Hello World!"

# awslocal sqs receive-message --queue-url http://localhost:4576/queue/status-queue

