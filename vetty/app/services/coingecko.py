from typing import Any, Dict, List, Optional
import httpx
from .config import settings


class CoinGeckoClient:
def __init__(self, base: str = settings.COINGECKO_BASE):
self.base = base


async def _get(self, path: str, params: Dict[str, Any] | None = None) -> Any:
url = f"{self.base}{path}"
async with httpx.AsyncClient(timeout=10.0) as client:
r = await client.get(url, params=params)
r.raise_for_status()
return r.json()


async def list_coins(self, page: int = 1, per_page: int = 10) -> List[Dict]:
params = {"vs_currency": "inr", "order": "market_cap_desc", "per_page": per_page, "page": page, "sparkline": False}
return await self._get('/coins/markets', params=params)


async def get_coin_by_id(self, coin_id: str, vs_currencies: List[str]=['inr','cad']) -> Dict:
# Coingecko: /coins/{id}
return await self._get(f"/coins/{coin_id}", params={"localization": False, "tickers": False, "market_data": True, "community_data": False, "developer_data": False, "sparkline": False})


async def list_categories(self) -> List[Dict]:
return await self._get('/coins/categories')


async def coins_in_category(self, category_id: str, page: int = 1, per_page: int = 10) -> List[Dict]:
# CoinGecko exposes /coins/markets with category filter
params = {"vs_currency": "inr", "category": category_id, "per_page": per_page, "page": page}
return await self._get('/coins/markets', params=params)


client = CoinGeckoClient()