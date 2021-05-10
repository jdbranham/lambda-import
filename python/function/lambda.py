import os
import logging
import jsonpickle
import boto3
from aws_xray_sdk.core import xray_recorder
from aws_xray_sdk.core import patch_all

# boto3 S3 initialization
s3_client = boto3.client("s3")

logger = logging.getLogger()
logger.setLevel(logging.INFO)
patch_all()

client = boto3.client('lambda')
client.get_account_settings()

# Get environment variables
DESTINATION_BUCKET = os.getenv('DESTINATION_BUCKET')

def lambda_handler(event, context):
    logger.info('## ENVIRONMENT VARIABLES\r' + jsonpickle.encode(dict(**os.environ)))
    logger.info('## EVENT\r' + jsonpickle.encode(event))
    logger.info('## CONTEXT\r' + jsonpickle.encode(context))

    # event contains all information about uploaded object
    print("Event :", event)

    # Bucket Name where file was uploaded
    source_bucket_name = event['Records'][0]['s3']['bucket']['name']

    # Filename of object (with path)
    file_key_name = event['Records'][0]['s3']['object']['key']

    # Copy Source Object
    #copy_source_object = {'Bucket': source_bucket_name, 'Key': file_key_name}

    # S3 copy object operation
    #s3_client.copy_object(CopySource=copy_source_object, Bucket=destination_bucket_name, Key=file_key_name)

    return {
        'statusCode': 200,
        'body': json.dumps('Hello from S3 events Lambda!')
    }