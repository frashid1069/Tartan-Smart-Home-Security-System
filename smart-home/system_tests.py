import requests
from bs4 import BeautifulSoup

SERVER_URL = "http://localhost:8080"
STATE_ENDPOINT = f"{SERVER_URL}/smarthome/state"
UPDATE_ENDPOINT = f"{SERVER_URL}/smarthome/update"
CREDENTIALS = ('admin', '1234')

def fetch_html_content(endpoint, auth):
    reply = requests.get(endpoint, auth=auth)
    assert reply.status_code == 200
    assert reply.headers['content-type'].startswith("text/html")
    return BeautifulSoup(reply.text, 'html.parser')

class TestSmartHomeResource:

    def test_state_retrieval(self):
        reply = requests.get(f"{STATE_ENDPOINT}/mse", auth=CREDENTIALS)
        assert reply.status_code == 200
        assert reply.headers['content-type'].startswith("text/html")
        parsed_html = BeautifulSoup(reply.text, 'html.parser')
        assert parsed_html.find('div', {'id': 'mse_div'}) is not None

    def test_nonexistent_house(self):
        reply = requests.get(f"{STATE_ENDPOINT}/nonexistenthouse", auth=CREDENTIALS)
        assert reply.status_code == 204

    def test_modify_state(self):
        data = {"name": "mse"}
        headers = {'Content-type': 'application/json'}
        reply = requests.post(f"{UPDATE_ENDPOINT}/mse", auth=CREDENTIALS, headers=headers, json=data)
        assert reply.status_code == 200

    def test_auto_unlock(self):
        data = {"name": "mse", "door": "closed", "doorLock": "closed", "userInProximity": "empty"}
        headers = {'Content-type': 'application/json'}
        reply = requests.post(f"{UPDATE_ENDPOINT}/mse", auth=CREDENTIALS, headers=headers, json=data)
        assert reply.status_code == 200

        parsed_html = fetch_html_content(f"{STATE_ENDPOINT}/mse", CREDENTIALS)
        lock_status = parsed_html.find('span', {'id': 'doorLock'})
        assert lock_status is not None and lock_status.text.strip() == "Locked"

        data = {"name": "mse", "temperature": 22, "userInProximity": "occupied"}
        reply = requests.post(f"{UPDATE_ENDPOINT}/mse", auth=CREDENTIALS, headers=headers, json=data)
        assert reply.status_code == 200

        parsed_html = fetch_html_content(f"{STATE_ENDPOINT}/mse", CREDENTIALS)
        lock_status = parsed_html.find('span', {'id': 'doorLock'})
        assert lock_status is not None and lock_status.text.strip() == "Unlocked"

    def test_electronic_operations(self):
        data = {"name": "mse", "doorLock": "closed"}
        headers = {'Content-type': 'application/json'}
        reply = requests.post(f"{UPDATE_ENDPOINT}/mse", auth=CREDENTIALS, headers=headers, json=data)
        assert reply.status_code == 200

        parsed_html = fetch_html_content(f"{STATE_ENDPOINT}/mse", CREDENTIALS)
        lock_status = parsed_html.find('span', {'id': 'doorLock'})
        assert lock_status is not None and lock_status.text.strip() == "Locked"

        data = {"name": "mse", "doorLock": "open", "doorLockPasscode": "incorrect"}
        reply = requests.post(f"{UPDATE_ENDPOINT}/mse", auth=CREDENTIALS, headers=headers, json=data)
        assert reply.status_code == 200

        parsed_html = fetch_html_content(f"{STATE_ENDPOINT}/mse", CREDENTIALS)
        lock_status = parsed_html.find('span', {'id': 'doorLock'})
        assert lock_status is not None and lock_status.text.strip() == "Locked"

        data = {"name": "mse", "doorLock": "open", "doorLockPasscode": "correct"}
        reply = requests.post(f"{UPDATE_ENDPOINT}/mse", auth=CREDENTIALS, headers=headers, json=data)
        assert reply.status_code == 200

        parsed_html = fetch_html_content(f"{STATE_ENDPOINT}/mse", CREDENTIALS)
        lock_status = parsed_html.find('span', {'id': 'doorLock'})
        assert lock_status is not None and lock_status.text.strip() == "Unlocked"

    def test_intruder_defense(self):
        data = {"name": "mse", "door": "closed", "doorLock": "open", "intruderDetected": "inactive", "allClearSignal": "off"}
        headers = {'Content-type': 'application/json'}
        reply = requests.post(f"{UPDATE_ENDPOINT}/mse", auth=CREDENTIALS, headers=headers, json=data)
        assert reply.status_code == 200

        parsed_html = fetch_html_content(f"{STATE_ENDPOINT}/mse", CREDENTIALS)
        lock_status = parsed_html.find('span', {'id': 'doorLock'})
        assert lock_status is not None and lock_status.text.strip() == "Unlocked"

        intruder_status = parsed_html.find('select', {'id': 'intruderDetected'})
        assert intruder_status is not None and intruder_status.find('option', {'selected': True}).text.strip() == "No Intruder Detected"

        data = {"name": "mse", "intruderDetected": "active", "door": "closed", "doorLock": "open"}
        reply = requests.post(f"{UPDATE_ENDPOINT}/mse", auth=CREDENTIALS, headers=headers, json=data)
        assert reply.status_code == 200

        parsed_html = fetch_html_content(f"{STATE_ENDPOINT}/mse", CREDENTIALS)
        lock_status = parsed_html.find('span', {'id': 'doorLock'})
        assert lock_status is not None and lock_status.text.strip() == "Locked"

        intruder_status = parsed_html.find('select', {'id': 'intruderDetected'})
        assert intruder_status is not None and intruder_status.find('option', {'selected': True}).text.strip() == "Intruder Detected"

        data = {"name": "mse", "allClearSignal": "on"}
        reply = requests.post(f"{UPDATE_ENDPOINT}/mse", auth=CREDENTIALS, headers=headers, json=data)
        assert reply.status_code == 200

        parsed_html = fetch_html_content(f"{STATE_ENDPOINT}/mse", CREDENTIALS)
        intruder_status = parsed_html.find('select', {'id': 'intruderDetected'})
        assert intruder_status is not None and intruder_status.find('option', {'selected': True}).text.strip() == "No Intruder Detected"