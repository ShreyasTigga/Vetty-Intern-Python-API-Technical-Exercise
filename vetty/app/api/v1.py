from fastapi import APIRouter, Depends, Query, HTTPException
from typing import List
from ..services.coingecko import client
from ..schemas import CoinMarket, Category
from ..auth import verify_token
from ..config import settings


router = APIRouter(prefix="/api/v1", tags=["v1"])


@router.get('/coins', response_model=List[CoinMarket])
async def list_coins(page_num: int = Query(1, alias='page_num', ge=1), per_page: int = Query(settings.DEFAULT_PER_PAGE, alias='per_page', ge=1, le=100), _token=Depends(verify_token)):
data = await client.list_coins(page=page_num, per_page=per_page)
# Map only required fields
return [
{
'id': c['id'],
'symbol': c.get('symbol'),
'name': c.get('name'),
'current_price': c.get('current_price'),
'market_cap': c.get('market_cap'),
'market_cap_rank': c.get('market_cap_rank'),
}
for c in data
]


@router.get('/categories', response_model=List[Category])
async def list_categories(_token=Depends(verify_token)):
data = await client.list_categories()
return [
{'id': c.get('id') or c.get('name'), 'name': c.get('name'), 'market_cap': c.get('market_cap')}
for c in data
]


@router.get('/coins/{coin_id}')
async def coin_detail(coin_id: str, _token=Depends(verify_token)):
data = await client.get_coin_by_id(coin_id)
if not data:
raise HTTPException(status_code=404, detail='Coin not found')
market = data.get('market_data', {})
# Provide prices in INR and CAD if present
prices = {}
for cur in ['inr','cad']:
prices[cur] = market.get('current_price', {}).get(cur)
return {'id': data.get('id'), 'name': data.get('name'), 'symbol': data.get('symbol'), 'prices': prices}


@router.get('/categories/{category_id}/coins')
async def coins_by_category(category_id: str, page_num: int = Query(1, alias='page_num', ge=1), per_page: int = Query(settings.DEFAULT_PER_PAGE, alias='per_page', ge=1, le=100), _token=Depends(verify_token)):
data = await client.coins_in_category(category_id, page=page_num, per_page=per_page)
return [
{'id': c['id'], 'symbol': c.get('symbol'), 'name': c.get('name'), 'current_price': c.get('current_price')}
for c in data
]