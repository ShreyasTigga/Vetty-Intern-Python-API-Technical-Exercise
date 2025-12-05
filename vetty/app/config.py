from pydantic import BaseSettings


class Settings(BaseSettings):
APP_NAME: str = "Vetty Crypto API"
VERSION: str = "1.0.0"
SECRET_KEY: str
ALGORITHM: str = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES: int = 60
COINGECKO_BASE: str = "https://api.coingecko.com/api/v3"
DEFAULT_PER_PAGE: int = 10


class Config:
env_file = ".env"


settings = Settings()