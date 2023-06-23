.PHONY: install_minikube install_kubernetes_cli \
    kubectl_version ingress_nginx_check \
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
    bank_management_service_ip postgres_service_ip prometheus_service_ip grafana_service_ip \
    get_nodes get_namespaces get_secrets get_configmaps get_persistent_volumes \
    get_pods get_services \
    show_commons show_components show_all \
    create_prometheus delete_prometheus \
    create_grafana delete_grafana

install_minikube:
	brew install minikube || true

install_kubernetes_cli:
	brew install kubernetes-cli || true

kubectl_version:
	kubectl version

ingress_nginx_check:
	kubectl get pods -n ingress-nginx

install: install_minikube install_kubernetes_cli

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
	kubectl apply -f namespace.yaml

delete_namespaces:
	kubectl delete -f namespace.yaml

create_secret_for_docker:
	kubectl create secret generic dockerhub-secret-temp \
		--from-file=.dockerconfigjson=config.json \
		--type=kubernetes.io/dockerconfigjson

create_secrets:
	kubectl apply -f secrets.yaml -n stage

delete_secrets:
	kubectl delete -f secrets.yaml -n stage

create_config_maps:
	kubectl apply -f config-maps.yaml -n stage

delete_config_maps:
	kubectl delete -f config-maps.yaml -n stage

create_persistent_volumes:
	kubectl apply -f persistent-volumes.yaml -n stage

delete_persistent_volumes:
	kubectl delete -f persistent-volumes.yaml -n stage

create_postgres:
	kubectl apply -f postgres-deployment.yaml -n stage

delete_postgres:
	kubectl delete -f postgres-deployment.yaml -n stage

create_bank_management:
	kubectl apply -f bank-management-deployment.yaml -n stage

delete_bank_management:
	kubectl delete -f bank-management-deployment.yaml -n stage

create_prometheus:
	kubectl apply -f prometheus-deployment.yaml -n stage

delete_prometheus:
	kubectl delete -f prometheus-deployment.yaml -n stage

create_grafana:
	kubectl apply -f grafana-deployment.yaml -n stage

delete_grafana:
	kubectl delete -f grafana-deployment.yaml -n stage

create_commons: create_namespaces create_config_maps create_secrets	create_persistent_volumes

create_components: create_postgres create_bank_management create_prometheus create_grafana

restart_server: delete start

restart_server_data: delete start create_commons create_components

restart_data: delete_components create_components

kill_pods:
	kubectl delete pods --all -n stage

delete_components: delete_prometheus delete_grafana delete_bank_management delete_postgres

get_nodes:
	kubectl get nodes

get_namespaces:
	kubectl get namespaces

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

prometheus_service_ip:
	minikube service prometheus-service -n stage

grafana_service_ip:
	minikube service grafana-service -n stage