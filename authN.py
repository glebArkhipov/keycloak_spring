import requests
import json
from hashlib import sha256
import os
from base64 import urlsafe_b64encode
import pprint


TOKEN_URL = 'http://localhost:8484/auth/realms/my_realm/protocol/openid-connect/token'


def generate_code_verifier_and_challenge():
    code_verifier_not_encoded = os.urandom(32)
    code_verifier = urlsafe_b64encode(code_verifier_not_encoded).rstrip(b'=')
    challenge_not_encoded = sha256(code_verifier).digest()
    code_challenge = urlsafe_b64encode(challenge_not_encoded).rstrip(b'=')
    return {"code_challenge": code_challenge, "code_verifier": code_verifier}


def get_tokens_by_login_and_password():
    data = {
        'username': 'test',
        'password': 'test',
        'grant_type': 'password',
        'client_id': 'my_realm',
    }
    return json.loads(requests.post(TOKEN_URL, data=data).content)


def get_tokens_by(access_code):
    data = {
        'code': access_code,
        'redirect_uri': 'http://localhost:8080/not/important/now',
        'grant_type': 'authorization_code',
        'client_id': 'my_realm',
        'code_verifier': 'P6A0iMSv5d5sOyUGpEzdbAR71qL_7Ar1-GGwA7Av3Bk'
    }
    return json.loads(requests.post(TOKEN_URL, data=data).content)


def call_user_endpoint():
    access_token = get_tokens_by_login_and_password()['access_token']
    headers = {'Authorization': f'Bearer {access_token}'}
    resp = requests.get('http://localhost:8080/user', headers=headers).content
    print(resp)


if __name__ == "__main__":
    print(generate_code_verifier_and_challenge())
    # print(get_tokens_by_login_and_password()['access_token'])
    # pprint.pprint(get_tokens_by('27bb1420-26b1-44d0-b9bb-8f4cb2574b29.f01fdbe0-116a-4a11-854c-82ec61e6483f.my_realm'))

# url to login page
# http://localhost:8484/auth/realms/my_realm/protocol/openid-connect/auth?response_type=code&client_id=my_realm&redirect_uri=http://localhost:8080/not/important/now&code_challenge=Y4fhKdeI8y4rCBTJlfER7bUDrDAYx5LDJRMzVSeDibk&code_challenge_method=S256
