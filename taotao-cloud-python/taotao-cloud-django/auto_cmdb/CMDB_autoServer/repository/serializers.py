from rest_framework import serializers
from rest_framework.exceptions import ValidationError
from . import models


class MySerializer(serializers.Serializer):
    id = serializers.IntegerField(read_only=True)
    hostname = serializers.CharField(required=True)

    def validate_hostname(self, value):
        print(value)
        s = str(value)
        if not s.endswith('com'):
            raise ValidationError('必须以"com"结尾')
        return value

    def create(self, validated_data):
        print(validated_data)
        return models.Server.objects.create(**validated_data)

    def update(self, instance, validated_data):
        instance.hostname = validated_data.get('hosrname', instance.hostname)
        instance.save()
        return instance
