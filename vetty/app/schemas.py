from pydantic import BaseModel
from typing import Optional, List


class CoinBase(BaseModel):
    id: str
    symbol: str
    name: str


class CoinMarket(CoinBase):
    current_price: Optional[float]
    market_cap: Optional[float]
    market_cap_rank: Optional[int]


class Category(BaseModel):
    id: str
    name: str
    market_cap: Optional[float]
