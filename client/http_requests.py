import requests
import json

### CHANGE THIS AS NEEDED
SERVER_URL = "http://localhost:8080"

### DO NOT CHANGE ANYTHING BELOW THIS
group_endpoint = "/group"
group_list_endpoint = "/all"
user_endpoint = "/user"
balance_endpoint = "/balance"
balance_increase_endpoint = "/increase"
balance_decrease_endpoint = "/decrease"
payment_endpoint = "/payment"
group_payment_endpoint = "/group"
detailed_payment_endpoint = "/detailed"


def group_create(args):
    r = requests.post(SERVER_URL + group_endpoint, json={'name': args.name})
    print(r.status_code, r.reason)
    print(r.text)

def group_list(args):
    r = requests.get(SERVER_URL + group_endpoint + group_list_endpoint)
    print(r.status_code, r.reason)
    print(r.text)

def group_get(args):
    r = requests.get(SERVER_URL + group_endpoint, params={'name': args.name})
    print(r.status_code, r.reason)
    print(r.text)

def group_update(args):
    r = requests.put(SERVER_URL + group_endpoint, params={'name': args.name}, json={'name': args.updated_name})
    print(r.status_code, r.reason)
    print(r.text)

def group_delete(args):
    r = requests.delete(SERVER_URL + group_endpoint, params={'name': args.name})
    print(r.status_code, r.reason)
    print(r.status_code, r.reason)


def user_create(args):
    r = requests.post(SERVER_URL + user_endpoint, params={'group': args.group}, json={'username': args.username})
    print(r.status_code, r.reason)
    print(r.text)

def user_list(args):
    r = requests.get(SERVER_URL + user_endpoint, params={'group': args.group})
    print(r.status_code, r.reason)
    print(r.text)

def user_update(args):
    r = requests.put(SERVER_URL + user_endpoint, params={'group': args.group, 'username': args.username}, json={'username': args.updated_username, 'balance': args.balance})
    print(r.status_code, r.reason)
    print(r.text)

def user_delete(args):
    r = requests.delete(SERVER_URL + user_endpoint, params={'group': args.group, 'username': args.username})
    print(r.status_code, r.reason)
    print(r.status_code, r.reason)


def balance_increase(args):
    r = requests.put(SERVER_URL + balance_endpoint + balance_increase_endpoint, params={'group': args.group, 'username': args.username, 'amount': args.amount})
    print(r.status_code, r.reason)
    print(r.text)

def balance_decrease(args):
    r = requests.put(SERVER_URL + balance_endpoint + balance_decrease_endpoint, params={'group': args.group, 'username': args.username, 'amount': args.amount})
    print(r.status_code, r.reason)
    print(r.text)

def balance_payment(args):
    r = requests.put(SERVER_URL + balance_endpoint + payment_endpoint, params={'group': args.group, 'payer': args.payer, 'payee': args.payee, 'amount': args.amount})
    print(r.status_code, r.reason)
    print(r.text)

def balance_group_payment(args):
    r = requests.put(SERVER_URL + balance_endpoint + payment_endpoint + group_payment_endpoint, params={'group': args.group, 'payer': args.payer, 'amount': args.amount})
    print(r.status_code, r.reason)
    print(r.text)

def balance_detailed_payment(args):
    body_str = "["
    for payment in args.payment_details:
        body_str += json.dumps(payment.__dict__) + ","
    body_str = body_str[:-1] + "]"

    r = requests.put(SERVER_URL + balance_endpoint + payment_endpoint + detailed_payment_endpoint, params={'group': args.group, 'payer': args.payer}, json=json.loads(body_str))
    print(r.status_code, r.reason)
    print(r.text)