.PHONY: install_minikube install_kubernetes_cli install_helm \
    install start stop update delete \
    restart_server restart_data restart_server_data \
    kill_pods \
    show_addons \
    dashboard \
    create_namespaces delete_namespaces \
    create_secrets delete_secrets \
    create_config_maps delete_config_maps \
    create_persistent_volumes delete_persistent_volumes \
    create_postgres delete_postgres \
    create_bank_management delete_bank_management \
    create_components delete_components show_components \
    bank_management_service_ip postgres_service_ip \
    get_nodes get_namespaces get_secrets get_configmaps get_persistent_volumes \
    get_pods get_services \
    show_commons show_components show_all

install_minikube:
	brew install minikube || true

install_kubernetes_cli:
	brew install kubernetes-cli || true

install_helm:
	brew install helm || true

install: install_minikube install_kubernetes_cli install_helm

start: install
	minikube start
	minikube addons enable ingress
	minikube addons enable dashboard
	minikube addons enable metrics-server

show_addons:
	minikube addons list

dashboard:
	minikube dashboard

stop:
	minikube stop

delete:
	minikube delete

update:
	minikube update-check

create_namespaces:
	kubectl apply -f commons/namespace.yaml

delete_namespaces:
	kubectl delete -f commons/namespace.yaml

create_secrets:
	kubectl apply -f commons/secrets.yaml -n stage

delete_secrets:
	kubectl delete -f commons/secrets.yaml -n stage

create_config_maps:
	kubectl apply -f commons/config-maps.yaml -n stage

delete_config_maps:
	kubectl delete -f commons/config-maps.yaml -n stage

create_persistent_volumes:
	kubectl apply -f commons/persistent-volumes.yaml -n stage

delete_persistent_volumes:
	kubectl delete -f commons/persistent-volumes.yaml -n stage

create_postgres:
	helm install postgres ./postgres-chart

delete_postgres:
	helm uninstall postgres

create_bank_management:
	helm install bank-management ./bank-management-chart

delete_bank_management:
	helm uninstall bank-management

create_commons: create_namespaces create_config_maps create_secrets	create_persistent_volumes

create_components: create_postgres create_bank_management

restart_server: delete start

restart_server_data: delete start create_commons create_components

restart_data: delete_components create_components

kill_pods:
	kubectl delete pods --all -n stage

delete_components: delete_bank_management delete_postgres

get_nodes:
	kubectl get nodes

get_namespaces:
	kubectl get ns

get_configmaps:
	kubectl get configmaps -n stage

get_secrets:
	kubectl get secrets -n stage

get_persistent_volumes:
	kubectl get pv -n stage

show_commons: get_nodes get_namespaces get_configmaps get_secrets get_persistent_volumes

get_pods:
	kubectl get pods -n stage

get_services:
	kubectl get services -n stage

show_components: get_pods get_services

show_all: show_commons show_components

bank_management_service_ip:
	minikube service bank-management-service -n stage

postgres_service_ip:
	minikube service postgres-service -n stage