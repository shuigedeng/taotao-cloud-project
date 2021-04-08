#_*_coding:utf-8_*_
__author__ = 'jieli'


from django.db import models
from django.contrib.auth.models import (
    BaseUserManager, AbstractBaseUser,Group,PermissionsMixin
)
import django

class UserManager(BaseUserManager):
    def create_user(self, email, name, password=None):
        """
        Creates and saves a User with the given email, date of
        birth and password.
        """
        if not email:
            raise ValueError('Users must have an email address')

        user = self.model(
            email=self.normalize_email(email),
            name=name,

        )

        user.set_password(password)
        user.save(using=self._db)
        return user

    def create_superuser(self, email, name ,password):
        """
        Creates and saves a superuser with the given email, date of
        birth and password.
        """
        user = self.create_user(email,
            password=password,
            name=name,
        )
        user.is_admin = True
        user.save(using=self._db)
        return user


