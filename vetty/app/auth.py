from datetime import datetime, timedelta
from jose import jwt, JWTError
from fastapi import HTTPException, status, Depends
from fastapi.security import OAuth2PasswordBearer
from .config import settings

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="/token")


def create_access_token(subject: str, expires_delta: int | None = None) -> str:
    """
    Create a JWT token.
    """
    to_encode = {"sub": subject}

    if expires_delta:
        expire = datetime.utcnow() + timedelta(minutes=expires_delta)
    else:
        expire = datetime.utcnow() + timedelta(minutes=settings.ACCESS_TOKEN_EXPIRE_MINUTES)

    to_encode.update({"exp": expire})

    return jwt.encode(to_encode, settings.SECRET_KEY, algorithm=settings.ALGORITHM)


def verify_token(token: str = Depends(oauth2_scheme)) -> str:
    """
    Validate JWT and return subject.
    """
    credentials_exception = HTTPException(
        status_code=status.HTTP_401_UNAUTHORIZED,
        detail="Could not validate credentials",
        headers={"WWW-Authenticate": "Bearer"},
    )

    try:
        payload = jwt.decode(token, settings.SECRET_KEY, algorithms=[settings.ALGORITHM])
        subject: str = payload.get("sub")
        if subject is None:
            raise credentials_exception
        return subject
    except JWTError:
        raise credentials_exception
