from fastapi import FastAPI, Depends
from .api.v1 import router as v1_router
from .config import settings
from fastapi.security import OAuth2PasswordRequestForm
from .auth import create_access_token
import httpx


app = FastAPI(title=settings.APP_NAME, version=settings.VERSION)


app.include_router(v1_router)


@app.post('/token')
async def login(form_data: OAuth2PasswordRequestForm = Depends()):
# For exercise: a simple single test user (username/password in env) or allow any user with a known password env var
# In prod replace with proper user store
if form_data.username != 'test' or form_data.password != 'test':
return {"error": "invalid credentials"}
token = create_access_token(subject=form_data.username)
return {"access_token": token, "token_type": "bearer"}


@app.get('/health')
async def health():
# Basic app health + 3rd party
async with httpx.AsyncClient() as client:
try:
r = await client.get(settings.COINGECKO_BASE + '/ping', timeout=3)
service_ok = r.status_code == 200
except Exception:
service_ok = False
return {'status': 'ok', 'coingecko': 'ok' if service_ok else 'unavailable'}


@app.get('/version')
async def version():
return {'app': settings.APP_NAME, 'version': settings.VERSION}