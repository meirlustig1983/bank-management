.PHONY: install_minikube install_kubernetes_cli \
    kubectl_version \
    install start stop update delete init restart_server restart_data restart_server_data \
    create_namespaces delete_namespaces \
    create_postgres delete_postgres \
    create_dockerhub_secret delete_dockerhub_secret \
    create_db_config_map delete_db_config_map \
    create_bank_management delete_bank_management \
    create_components delete_components show_components \
    bank_management_service_ip postgres_service_ip prometheus_service_ip grafana_service_ip \
    get_namespaces get_nodes get_pods get_services get_secrets get_ingress get_configmaps \
    create_prometheus delete_prometheus \
    create_grafana delete_grafana \
    create_ingress delete_ingress

install_minikube:
	brew install minikube || true

install_kubernetes_cli:
	brew install kubernetes-cli || true

kubectl_version:
	kubectl version

install: install_minikube install_kubernetes_cli

start: install
	minikube start

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

create_postgres:
	kubectl apply -f postgres-deployment.yaml -n stage

delete_postgres:
	kubectl delete -f postgres-deployment.yaml -n stage

create_secret_for_docker:
	kubectl create secret generic dockerhub-secret-temp \
		--from-file=.dockerconfigjson=config.json \
		--type=kubernetes.io/dockerconfigjson

create_dockerhub_secret:
	kubectl apply -f dockerhub-secret.yaml -n stage

delete_dockerhub_secret:
	kubectl delete -f dockerhub-secret.yaml -n stage

create_db_config_map:
	kubectl apply -f db-config-map.yaml -n stage

delete_db_config_map:
	kubectl delete -f db-config-map.yaml -n stage

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

create_components: create_namespaces create_postgres create_dockerhub_secret create_db_config_map create_bank_management create_prometheus create_grafana

init: start create_components

restart_server: delete start

restart_server_data: delete init

restart_data: delete_components create_components

delete_components: delete_postgres delete_dockerhub_secret delete_db_config_map delete_bank_management delete_prometheus delete_grafana delete_namespaces

get_namespaces:
	kubectl get namespaces

get_configmaps:
	kubectl get configmaps -n stage

get_secrets:
	kubectl get secrets -n stage

get_nodes:
	kubectl get nodes

get_pods:
	kubectl get pods -n stage

get_services:
	kubectl get services -n stage

get_ingress:
	kubectl get ingress -n stage

show_components: get_namespaces get_configmaps get_secrets get_nodes get_pods get_services get_ingress

bank_management_service_ip:
	minikube service bank-management-service -n stage

postgres_service_ip:
	minikube service postgres-service -n stage

prometheus_service_ip:
	minikube service prometheus-service -n stage

grafana_service_ip:
	minikube service grafana-service -n stage









create_ingress:
	kubectl apply -f ingress-deployment.yaml -n stage

delete_ingress:
	kubectl delete -f ingress-deployment.yaml -n stage

