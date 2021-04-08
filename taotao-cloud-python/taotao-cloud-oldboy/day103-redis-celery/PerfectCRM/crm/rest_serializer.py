



from crm import models
from rest_framework import serializers

# Serializers define the API representation.
class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.UserProfile
        depth = 3
        fields = ('url', 'email', 'name', 'is_staff','is_active','role')


class RoleSerializer(serializers.ModelSerializer):
    class Meta:
        model = models.Role
        fields = ("name",)