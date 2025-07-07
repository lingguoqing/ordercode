from django.db import models

# Create your models here.

class Department(models.Model):
    name = models.CharField(verbose_name='部门名称', max_length=64)