
from rest_framework import  viewsets
from crm.rest_serializer import UserSerializer,RoleSerializer
from crm import models
# ViewSets define the view behavior.
class UserViewSet(viewsets.ModelViewSet):
    queryset = models.UserProfile.objects.all()
    serializer_class = UserSerializer



class RoleViewSet(viewsets.ModelViewSet):
    queryset = models.Role.objects.all()
    serializer_class = RoleSerializer
