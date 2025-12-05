import pytest
from fastapi.testclient import TestClient
from app.main import app
import respx
from httpx import Response


client = TestClient(app)


@pytest.fixture
def token():
# create valid token by hitting /token endpoint
r = client.post('/token', data={'username':'test','password':'test'})
return r.json()['access_token']


@respx.mock
def test_list_coins(token):
# mock coingecko markets endpoint
respx.get('https://api.coingecko.com/api/v3/coins/markets').mock(return_value=Response(200, json=[{
'id': 'bitcoin','symbol':'btc','name':'Bitcoin','current_price':3000000,'market_cap':600000000000,'market_cap_rank':1
}]))


r = client.get('/api/v1/coins', headers={'Authorization': f'Bearer {token}'})
assert r.status_code == 200
data = r.json()
assert isinstance(data, list)
assert data[0]['id'] == 'bitcoin'